/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import de.webstore.backend.dto.ProductDTO;
import de.webstore.backend.dto.ProductUpdateDTO;
import de.webstore.backend.exception.ProductInOrderException;
import de.webstore.backend.exception.ProductNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.webstore.backend.config.DatabaseConnection;

/**
 * Service class responsible for managing product-related operations.
 * <p>
 * This class provides functionality to add, update, find, and delete products. It interfaces with the database
 * to perform these operations, ensuring business rules are adhered to, such as not allowing the deletion of products
 * that are part of an order.
 */
@Service
public class ProductService {

    private final DatabaseConnection databaseConnection;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductService(DatabaseConnection databaseConnection, JdbcTemplate jdbcTemplate) {
        this.databaseConnection = databaseConnection;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all products from the database.
     *
     * @return a list of all products
     */
    public List<ProductDTO> findAll() {
        List<ProductDTO> products = new ArrayList<>();
        String sql = "SELECT produktnummer, name, einheit, preis FROM produkt";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ProductDTO product = new ProductDTO();
                product.setProductId(rs.getString("produktnummer"));
                product.setName(rs.getString("name"));
                product.setUnit(rs.getString("einheit"));
                product.setPrice(rs.getBigDecimal("preis"));
                products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Finds a specific product by its ID.
     *
     * @param productId the product ID
     * @return the found product or null if not found
     */
    public ProductDTO findById(String productId) {
        ProductDTO product = null;
        String sql = "SELECT produktnummer, name, einheit, preis FROM produkt WHERE produktnummer = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new ProductDTO();
                    product.setProductId(rs.getString("produktnummer"));
                    product.setName(rs.getString("name"));
                    product.setUnit(rs.getString("einheit"));
                    product.setPrice(rs.getBigDecimal("preis"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Adds a new product to the database.
     *
     * @param productDTO the product to be added
     * @return the added product with its generated ID
     */
    public ProductDTO addProduct(ProductDTO productDTO) {
        // Generate a new UUID for the product
        String uuid = UUID.randomUUID().toString();
        productDTO.setProductId(uuid); // Set autogenerated UUID as product ID
        String sql = "INSERT INTO produkt (produktnummer, name, einheit, preis) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, uuid);
            pstmt.setString(2, productDTO.getName());
            pstmt.setString(3, productDTO.getUnit());
            pstmt.setBigDecimal(4, productDTO.getPrice());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productDTO.setProductId(generatedKeys.getString(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Handle the exception appropriately
        }
        return productDTO;
    }

    /**
     * Updates the attributes of an existing product based on its ID.
     *
     * @param productId         the ID of the product to update
     * @param productDTO the updated product information
     * @return the updated product
     */
    public ProductUpdateDTO updateProduct(String productId, ProductUpdateDTO productUpdateDTO) {
        String sql = "UPDATE produkt SET name = ?, einheit = ?, preis = ? WHERE produktnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productUpdateDTO.getName());
            pstmt.setString(2, productUpdateDTO.getUnit());
            pstmt.setBigDecimal(3, productUpdateDTO.getPrice());
            pstmt.setString(4, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productUpdateDTO;
    }

    /**
     * Deletes a product from the database, ensuring it is not part of any order position.
     * Throws ProductInOrderException if the product is referenced in any order.
     * Throws ProductNotFoundException if the product does not exist.
     *
     * @param productId the ID of the product to delete
     * @throws ProductInOrderException if the product is part of an order
     * @throws ProductNotFoundException if the product does not exist
     */
    public void deleteProduct(String productId) throws ProductInOrderException, ProductNotFoundException {
        Connection conn = null;
        PreparedStatement checkPositionStmt = null;
        PreparedStatement deleteLagertStmt = null;
        PreparedStatement deleteProduktlagermengeStmt = null;
        PreparedStatement deleteProduktStmt = null;
        ResultSet rs = null;

        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Check whether the product is used in any order items
            String checkPositionSql = "SELECT COUNT(*) AS count FROM position WHERE produktnummer = ?";
            checkPositionStmt = conn.prepareStatement(checkPositionSql);
            checkPositionStmt.setString(1, productId);
            rs = checkPositionStmt.executeQuery();
            if (rs.next() && rs.getInt("count") > 0) {
                throw new ProductInOrderException("Product cannot be deleted as it occurs in order items.");
            }

            // Delete references from 'lagert'
            String deleteLagertSql = "DELETE FROM lagert WHERE produkt_fk = ?";
            deleteLagertStmt = conn.prepareStatement(deleteLagertSql);
            deleteLagertStmt.setString(1, productId);
            deleteLagertStmt.executeUpdate();

            // Delete references from 'produktlagermenge'
            String deleteProduktlagermengeSql = "DELETE FROM produktlagermenge WHERE produkt_fk = ?";
            deleteProduktlagermengeStmt = conn.prepareStatement(deleteProduktlagermengeSql);
            deleteProduktlagermengeStmt.setString(1, productId);
            deleteProduktlagermengeStmt.executeUpdate();

            // Deleting the product
            String deleteProduktSql = "DELETE FROM produkt WHERE produktnummer = ?";
            deleteProduktStmt = conn.prepareStatement(deleteProduktSql);
            deleteProduktStmt.setString(1, productId);
            int affectedRows = deleteProduktStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ProductNotFoundException("No product with the ID: " + productId + " found.");
            }

            conn.commit(); // Commit transaction
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback(); // Rollback in the event of an error
                // Depending on the exception, throw an appropriate application-level exception
                if (e.getMessage().contains("Product cannot be deleted")) {
                    throw new ProductInOrderException(e.getMessage());
                } else {
                    throw new ProductNotFoundException(e.getMessage());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            // Close all resources
            try {
                if (rs != null) rs.close();
                if (checkPositionStmt != null) checkPositionStmt.close();
                if (deleteLagertStmt != null) deleteLagertStmt.close();
                if (deleteProduktlagermengeStmt != null) deleteProduktlagermengeStmt.close();
                if (deleteProduktStmt != null) deleteProduktStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Restore auto-commit
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Checks if a product with the specified ID exists in the database.
     * 
     * @param productId The ID of the product to check.
     * @return true if the product exists, false otherwise.
     */
    @SuppressWarnings("deprecation")
    public boolean exists(String productId) {
        String sql = "SELECT COUNT(*) FROM produkt WHERE produktnummer = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
        return count != null && count > 0;
    }
}
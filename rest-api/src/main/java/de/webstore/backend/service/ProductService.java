/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import de.webstore.backend.dto.ProductDTO;
import de.webstore.backend.dto.ProductUpdateDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    public ProductService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
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
                product.setProductNumber(rs.getInt("produktnummer"));
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
    public ProductDTO findById(int productId) {
        ProductDTO product = null;
        String sql = "SELECT produktnummer, name, einheit, preis FROM produkt WHERE produktnummer = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new ProductDTO();
                    product.setProductNumber(rs.getInt("produktnummer"));
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
        String sql = "INSERT INTO produkt (name, einheit, preis) VALUES (?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, productDTO.getName());
            pstmt.setString(2, productDTO.getUnit());
            pstmt.setBigDecimal(3, productDTO.getPrice());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        productDTO.setProductNumber(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    public ProductUpdateDTO updateProduct(int productId, ProductUpdateDTO productUpdateDTO) {
        String sql = "UPDATE produkt SET name = ?, einheit = ?, preis = ? WHERE produktnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, productUpdateDTO.getName());
            pstmt.setString(2, productUpdateDTO.getUnit());
            pstmt.setBigDecimal(3, productUpdateDTO.getPrice());
            pstmt.setInt(4, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return productUpdateDTO;
    }

    /**
     * Deletes a product from the database, ensuring it is not part of any order position.
     *
     * @param productId the ID of the product to delete
     * @return 
     */
    public boolean deleteProduct(int productId) {
        String checkSql = "SELECT COUNT(*) AS count FROM position WHERE produktnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt("count") == 0) {
                String deleteSql = "DELETE FROM produkt WHERE produktnummer = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, productId);
                    deleteStmt.executeUpdate();
                }
            } else {
                System.out.println("Product cannot be deleted as it is present in order positions.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
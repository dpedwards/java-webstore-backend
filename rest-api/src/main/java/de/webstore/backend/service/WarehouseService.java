/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import de.webstore.backend.config.DatabaseConnection;
import de.webstore.backend.dto.WarehouseDTO;
import de.webstore.backend.exception.ProductNotFoundException;

/**
 * Service class for managing warehouse-related operations such as finding,
 * adding, and reducing product quantities within the warehouse.
 */
@Service
public class WarehouseService {
    
    private final DatabaseConnection databaseConnection;
    private final JdbcTemplate jdbcTemplate;
    private final ProductService productService;

    @Autowired
    public WarehouseService(DatabaseConnection databaseConnection, JdbcTemplate jdbcTemplate, ProductService productService) {
        this.databaseConnection = databaseConnection;
        this.jdbcTemplate = jdbcTemplate;
        this.productService = productService;
    }


    public void updateWarehouseQuantities() {
        // SQL to calculate total product quantities per warehouse
        String updateSql = "UPDATE lager l JOIN " +
                           "(SELECT lager_fk, SUM(menge) AS totalMenge " +
                           "FROM produktlagermenge GROUP BY lager_fk) plm " +
                           "ON l.lagernummer = plm.lager_fk " +
                           "SET l.menge = plm.totalMenge " +
                           "WHERE l.aktiv = 1;";
    
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " rows updated.");
        } catch (SQLException e) {
            System.out.println("Error updating warehouse quantities: " + e.getMessage());
        }
    }

    /**
     * Retrieves all warehouse entries from the database.
     *
     * @return A list of all warehouse entries.
     */
    public List<WarehouseDTO> findAllActive() {
        // Update warehouse quantities before fetching
        updateWarehouseQuantities();
    
        List<WarehouseDTO> warehouses = new ArrayList<>();
        String sql = "SELECT lagernummer, menge, aktiv FROM lager WHERE aktiv = 1";
    
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                WarehouseDTO warehouse = new WarehouseDTO();
                warehouse.setWarehouseNumber(rs.getInt("lagernummer"));
                warehouse.setQuantity(rs.getInt("menge"));
                warehouse.setActive(rs.getBoolean("aktiv"));
                warehouses.add(warehouse);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return warehouses;
    }
    
    /**
     * Finds a specific warehouse entry by its ID.
     *
     * @param warehouseNumber The ID of the warehouse to find.
     * @return The found warehouse entry or null if not found.
     */
    public WarehouseDTO findById(int warehouseNumber) {
        WarehouseDTO warehouse = null;
        String sql = "SELECT * FROM lager WHERE lagernummer = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, warehouseNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    warehouse = new WarehouseDTO();
                    warehouse.setWarehouseNumber(rs.getInt("lagernummer")); 
                    warehouse.setQuantity(rs.getInt("menge"));
                    warehouse.setActive(rs.getBoolean("aktiv"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return warehouse;
    }

    /**
     * Adds a specified quantity of a product to the warehouse.
     *
     * @param productId The ID of the product.
     * @param warehouseNumber The Number of the warehouse.
     * @param menge The quantity to add.
     */
    public void addProductQuantity(String productId, int warehouseNumber, int menge) {
        // Update warehouse quantities before fetching
        updateWarehouseQuantities();

        // Attempt to update existing product quantity in the warehouse
        String updateSql = "UPDATE produktlagermenge SET menge = menge + ? WHERE produkt_fk = ? AND lager_fk = ?";
        // Fallback SQL to insert a new product quantity record if the update affects no rows
        String insertSql = "INSERT INTO produktlagermenge (produkt_fk, lager_fk, menge) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
            
            pstmtUpdate.setInt(1, menge);
            pstmtUpdate.setString(2, productId);
            pstmtUpdate.setInt(3, warehouseNumber);
            int affectedRows = pstmtUpdate.executeUpdate();

            // Update warehouse quantities before fetching
            updateWarehouseQuantities();
            
            // If no rows were affected by the update, try to insert a new record
            if (affectedRows == 0) {
                try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
                    pstmtInsert.setString(1, productId);
                    pstmtInsert.setInt(2, warehouseNumber);
                    pstmtInsert.setInt(3, menge);
                    pstmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reduces a specified quantity of a product in the warehouse. Ensures quantity does not go below zero.
     *
     * @param productId The ID of the product.
     * @param warehoseNumber The Number of the warehouse.
     * @param menge The quantity to reduce.
     */
    public void reduceProductQuantity(String productId, int warehouseNumber, int menge) {
        String sql = "UPDATE produktlagermenge SET menge = GREATEST(0, menge - ?) WHERE produkt_fk = ? AND lager_fk = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menge);
            pstmt.setString(2, productId);
            pstmt.setInt(3, warehouseNumber);
            pstmt.executeUpdate();

             // Update warehouse quantities before fetching
            updateWarehouseQuantities();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calculates the total quantity of a specified product across all warehouses.
     *
     * @param productId The ID of the product.
     * @return The total quantity of the product.
     */
    @SuppressWarnings("deprecation")
    public int calculateTotalProductQuantity(String productId) throws ProductNotFoundException {
        if (!productService.exists(productId)) {
            throw new ProductNotFoundException("Product with ID " + productId + " not found.");
        }
        String sql = "SELECT SUM(menge) FROM produktlagermenge WHERE produkt_fk = ?";
        Integer totalQuantity = jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
        return totalQuantity != null ? totalQuantity : 0;
    }

    /**
     * Checks if a warehouse with the specified Number exists in the database.
     * 
     * @param warehouseNumber The ID of the warehouse to check.
     * @return true if the warehouse exists, false otherwise.
     */
    @SuppressWarnings("deprecation")
    public boolean exists(int warehouseNumber) {
        String sql = "SELECT COUNT(*) FROM lager WHERE lagernummer = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{warehouseNumber}, Integer.class);
        return count != null && count > 0;
    }

    /**
     * Calculates the total quantity of all products stored in a specified warehouse.
     * <p>
     * This method queries the database to sum the quantities of all products located in a
     * specific warehouse identified by the {@code warehouseNumber}. It accesses the
     * {@code produktlagermenge} table, which contains the mappings between products and
     * warehouses along with the quantities of each product in each warehouse.
     *
     * @param warehouseNumber The unique identifier of the warehouse for which the total
     *                        product quantity is calculated. This identifier corresponds
     *                        to the {@code lagernummer} field in the {@code lager} table.
     * @return The total quantity of products in the specified warehouse. If the warehouse
     *         does not contain any products, or if an error occurs during the database
     *         query execution, this method returns 0.
     */
    public int sumProductsInWarehouse(int warehouseNumber) {
        String sql = "SELECT SUM(menge) AS totalQuantity FROM produktlagermenge WHERE lager_fk = ?";
        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, warehouseNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("totalQuantity");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Updates the total product quantities in all active warehouses.
     * <p>
     * This method iterates over a list of active warehouses, calculates the total
     * quantity of products for each warehouse using the {@link #sumProductsInWarehouse(int)}
     * method, and updates the respective warehouse entry in the database with the new total.
     * The method assumes that each warehouse has an {@code isActive} attribute in the database
     * which is used to determine if the warehouse is active.
     * <p>
     * The update is performed on the {@code lager} table, specifically updating the
     * {@code menge} (quantity) column for each warehouse based on its {@code lagernummer}
     * (warehouse number). It only updates warehouses that are marked as active.
     *
     * @param activeWarehouses A list of {@link WarehouseDTO} objects representing the
     *                         active warehouses whose quantities need to be updated.
     *                         Each {@link WarehouseDTO} must have the warehouse number
     *                         and its current quantity set.
     *                         
     * @throws SQLException If an SQL exception occurs during the update process. This
     *                      includes cases where the connection to the database fails,
     *                      the prepared statement cannot be executed, or the update
     *                      operation is otherwise unsuccessful. Callers should handle
     *                      this exception appropriately, possibly logging the error
     *                      and/or notifying an administrator if necessary.
     */
    public void updateWarehouseQuantities(List<WarehouseDTO> activeWarehouses) {
        String sql = "UPDATE lager SET menge = ? WHERE lagernummer = ? AND aktiv = TRUE"; // Assuming isActive column exists
        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (WarehouseDTO warehouse : activeWarehouses) {
                int totalQuantity = sumProductsInWarehouse(warehouse.getWarehouseNumber());
                pstmt.setInt(1, totalQuantity);
                pstmt.setInt(2, warehouse.getWarehouseNumber());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // It may be appropriate to rethrow the exception as a custom unchecked exception
            // or handle it based on your application's error handling policies.
        }
    }
}
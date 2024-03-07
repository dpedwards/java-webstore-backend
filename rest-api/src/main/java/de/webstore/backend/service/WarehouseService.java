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
import de.webstore.backend.exception.WarehouseNotFoundException;
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
     * Adds a specified quantity of a product to the warehouse and updates the warehouse total quantity.
     * Also ensures the product and warehouse mapping in the 'lagert' table.
     *
     * @param productId The ID of the product.
     * @param warehouseNumber The number of the warehouse.
     * @param menge The quantity to add.
     */
    public void addProductQuantityAndUpdateWarehouse(String productId, int warehouseNumber, int menge) {
        Connection conn = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtUpdateWarehouse = null;
        PreparedStatement pstmtInsertLagert = null;
        String checkLagertSql = "SELECT COUNT(*) FROM lagert WHERE produkt_fk = ? AND lager_fk = ?";
        String updateWarehouseSql = "UPDATE lager SET menge = menge + ? WHERE lagernummer = ?";
        String insertLagertSql = "INSERT INTO lagert (produkt_fk, lager_fk) VALUES (?, ?) ON DUPLICATE KEY UPDATE produkt_fk=VALUES(produkt_fk), lager_fk=VALUES(lager_fk)";

        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Check if product and warehouse exist
            if (!productAndWarehouseExist(productId, warehouseNumber)) {
                throw new WarehouseNotFoundException("Product or warehouse not found");
            }

            // Update the product quantity in the warehouse
            String updateSql = "UPDATE produktlagermenge SET menge = menge + ? WHERE produkt_fk = ? AND lager_fk = ?";
            String insertSql = "INSERT INTO produktlagermenge (produkt_fk, lager_fk, menge) VALUES (?, ?, ?)";

            pstmtUpdate = conn.prepareStatement(updateSql);
            pstmtUpdate.setInt(1, menge);
            pstmtUpdate.setString(2, productId);
            pstmtUpdate.setInt(3, warehouseNumber);
            int affectedRows = pstmtUpdate.executeUpdate();

            if (affectedRows == 0) {
                pstmtInsert = conn.prepareStatement(insertSql);
                pstmtInsert.setString(1, productId);
                pstmtInsert.setInt(2, warehouseNumber);
                pstmtInsert.setInt(3, menge);
                pstmtInsert.executeUpdate();
            }

            // Update the total quantity in the warehouse
            pstmtUpdateWarehouse = conn.prepareStatement(updateWarehouseSql);
            pstmtUpdateWarehouse.setInt(1, menge);
            pstmtUpdateWarehouse.setInt(2, warehouseNumber);
            pstmtUpdateWarehouse.executeUpdate();

            // Ensure the product and warehouse mapping in 'lagert' table
            try (PreparedStatement pstmtCheckLagert = conn.prepareStatement(checkLagertSql)) {
                pstmtCheckLagert.setString(1, productId);
                pstmtCheckLagert.setInt(2, warehouseNumber);
                ResultSet rs = pstmtCheckLagert.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    pstmtInsertLagert = conn.prepareStatement(insertLagertSql);
                    pstmtInsertLagert.setString(1, productId);
                    pstmtInsertLagert.setInt(2, warehouseNumber);
                    pstmtInsertLagert.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
                System.out.println("Rollback due to error: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("Error during rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (pstmtInsert != null) pstmtInsert.close();
                if (pstmtUpdateWarehouse != null) pstmtUpdateWarehouse.close();
                if (pstmtInsertLagert != null) pstmtInsertLagert.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Reduces a specified quantity of a product in the warehouse. Ensures quantity does not go below zero and updates the total warehouse quantity accordingly.
     *
     * @param productId The ID of the product.
     * @param warehouseNumber The number of the warehouse.
     * @param menge The quantity to reduce.
     */
    public void reduceProductQuantityAndUpdateWarehouse(String productId, int warehouseNumber, int menge) {
        Connection conn = null;
        PreparedStatement pstmtReduceProductQuantity = null;
        PreparedStatement pstmtUpdateWarehouse = null;

        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Check if product and warehouse exist
            if (!productAndWarehouseExist(productId, warehouseNumber)) {
                throw new WarehouseNotFoundException("Product or warehouse not found.");
            }

            // Reduce the product quantity in the warehouse
            String reduceProductQuantitySql = "UPDATE produktlagermenge SET menge = GREATEST(0, menge - ?) WHERE produkt_fk = ? AND lager_fk = ?";
            pstmtReduceProductQuantity = conn.prepareStatement(reduceProductQuantitySql);
            pstmtReduceProductQuantity.setInt(1, menge);
            pstmtReduceProductQuantity.setString(2, productId);
            pstmtReduceProductQuantity.setInt(3, warehouseNumber);
            pstmtReduceProductQuantity.executeUpdate();

            // Update the total quantity in the warehouse
            String updateWarehouseSql = "UPDATE lager SET menge = GREATEST(0, menge - ?) WHERE lagernummer = ?";
            pstmtUpdateWarehouse = conn.prepareStatement(updateWarehouseSql);
            pstmtUpdateWarehouse.setInt(1, menge);
            pstmtUpdateWarehouse.setInt(2, warehouseNumber);
            pstmtUpdateWarehouse.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
                System.out.println("Rollback due to error: " + e.getMessage());
            } catch (SQLException ex) {
                System.out.println("Error during rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (pstmtReduceProductQuantity != null) pstmtReduceProductQuantity.close();
                if (pstmtUpdateWarehouse != null) pstmtUpdateWarehouse.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    
    /**
     * Checks if the specified product and warehouse exist in the database.
     * 
     * This method performs two separate checks: one for the product and one for the warehouse.
     * It queries the `produkt` table with the given product ID and the `lager` table with the given warehouse number.
     * Both must exist for the method to return true.
     *
     * @param productId The unique identifier of the product.
     * @param warehouseNumber The unique number of the warehouse.
     * @return true if both the product and warehouse exist, false otherwise.
     */
    public boolean productAndWarehouseExist(String productId, int warehouseNumber) {
        // SQL queries to check the existence of product and warehouse
        String productExistsQuery = "SELECT COUNT(*) AS count FROM produkt WHERE produktnummer = ?";
        String warehouseExistsQuery = "SELECT COUNT(*) AS count FROM lager WHERE lagernummer = ?";

        try (Connection conn = databaseConnection.getConnection();
            // Prepare statements for both queries
            PreparedStatement productStmt = conn.prepareStatement(productExistsQuery);
            PreparedStatement warehouseStmt = conn.prepareStatement(warehouseExistsQuery)) {

            // Check product existence
            productStmt.setString(1, productId);
            ResultSet productRs = productStmt.executeQuery();
            boolean productExists = productRs.next() && productRs.getInt("count") > 0;
            
            // Check warehouse existence
            warehouseStmt.setInt(1, warehouseNumber);
            ResultSet warehouseRs = warehouseStmt.executeQuery();
            boolean warehouseExists = warehouseRs.next() && warehouseRs.getInt("count") > 0;

            // Both must exist
            return productExists && warehouseExists;
        } catch (SQLException e) {
            // Log error and handle it appropriately
            System.out.println("Database error in productAndWarehouseExist: " + e.getMessage());
            return false;
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
        }
    }
}
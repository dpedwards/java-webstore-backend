/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import de.webstore.backend.config.DatabaseConnection;
import de.webstore.backend.dto.WarehouseDTO;

/**
 * Service class for managing warehouse-related operations such as finding,
 * adding, and reducing product quantities within the warehouse.
 */
@Service
public class WarehouseService {
    
    private final DatabaseConnection databaseConnection;

    @Autowired
    public WarehouseService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Retrieves all warehouse entries from the database.
     *
     * @return A list of all warehouse entries.
     */
    public List<WarehouseDTO> findAll() {
        List<WarehouseDTO> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM lager";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                WarehouseDTO warehouse = new WarehouseDTO();
                warehouse.setWarehouseNumber(rs.getInt("lagernummer"));
                warehouse.setQuantity(rs.getInt("menge"));
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
     * @param id The ID of the warehouse to find.
     * @return The found warehouse entry or null if not found.
     */
    public WarehouseDTO findById(int id) {
        WarehouseDTO warehouse = null;
        String sql = "SELECT * FROM lager WHERE lagernummer = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    warehouse = new WarehouseDTO();
                    warehouse.setWarehouseNumber(rs.getInt("lagernummer")); 
                    warehouse.setQuantity(rs.getInt("menge"));
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
     * @param produktId The ID of the product.
     * @param orderId The ID of the warehouse.
     * @param menge The quantity to add.
     */
    public void addProductQuantity(int produktId, int orderId, int menge) {
        // Attempt to update existing product quantity in the warehouse
        String updateSql = "UPDATE produktlagermenge SET menge = menge + ? WHERE produkt_fk = ? AND lager_fk = ?";
        // Fallback SQL to insert a new product quantity record if the update affects no rows
        String insertSql = "INSERT INTO produktlagermenge (produkt_fk, lager_fk, menge) VALUES (?, ?, ?)";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
            
            pstmtUpdate.setInt(1, menge);
            pstmtUpdate.setInt(2, produktId);
            pstmtUpdate.setInt(3, orderId);
            int affectedRows = pstmtUpdate.executeUpdate();
            
            // If no rows were affected by the update, try to insert a new record
            if (affectedRows == 0) {
                try (PreparedStatement pstmtInsert = conn.prepareStatement(insertSql)) {
                    pstmtInsert.setInt(1, produktId);
                    pstmtInsert.setInt(2, orderId);
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
     * @param produktId The ID of the product.
     * @param orderId The ID of the warehouse.
     * @param menge The quantity to reduce.
     */
    public void reduceProductQuantity(int produktId, int orderId, int menge) {
        String sql = "UPDATE produktlagermenge SET menge = GREATEST(0, menge - ?) WHERE produkt_fk = ? AND lager_fk = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, menge);
            pstmt.setInt(2, produktId);
            pstmt.setInt(3, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Calculates the total quantity of a specified product across all warehouses.
     *
     * @param produktId The ID of the product.
     * @return The total quantity of the product.
     */
    public int calculateTotalProductQuantity(int produktId) {
        String sql = "SELECT SUM(menge) AS totalQuantity FROM produktlagermenge WHERE produkt_fk = ?";
        int totalQuantity = 0;
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, produktId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                totalQuantity = rs.getInt("totalQuantity");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalQuantity;
    }
}
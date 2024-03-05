/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import de.webstore.backend.dto.OrderDTO;
import de.webstore.backend.dto.PositionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import de.webstore.backend.config.DatabaseConnection;

/**
 * Service layer for managing orders.
 * <p>
 * This class provides services for retrieving, creating, and managing orders and their positions,
 * interfacing with the database to perform these operations.
 */
@Service
public class OrderService {

    // Database connection dependency injected by Spring
    private final DatabaseConnection databaseConnection;

    @Autowired
    public OrderService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    /**
     * Retrieves all orders from the database.
     *
     * @return a list of all orders
     */
    public List<OrderDTO> findAll() {
        List<OrderDTO> orders = new ArrayList<>();
        String sql = "SELECT * FROM auftrag";

        // Try-with-resources statement ensures that each resource is closed at the end of the statement
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                OrderDTO order = new OrderDTO();
                order.setOrderNumber(rs.getInt("auftragsnummer"));
                order.setDate(rs.getDate("Datum").toLocalDate());
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    /**
     * Finds a specific order by its ID.
     *
     * @param id the order ID
     * @return the found order or null if not found
     */
    public OrderDTO findById(int id) {
        OrderDTO order = null;
        String sql = "SELECT * FROM auftrag WHERE auftragsnummer = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new OrderDTO();
                    order.setOrderNumber(rs.getInt("auftragsnummer"));
                    order.setDate(rs.getDate("datum").toLocalDate());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return order;
    }

    /**
     * Creates a new order in the database.
     *
     * @param orderDTO the order to be created
     * @return the created order with its new ID
     */
    public OrderDTO createOrder(OrderDTO orderDTO) {
        String sql = "INSERT INTO auftrag (datum, status) VALUES (?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(orderDTO.getDate()));
            pstmt.setString(2, "offen");  // status is always 'offen' when creating a new order
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderDTO.setOrderNumber(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderDTO;
    }

    /**
     * Adds a position to an existing order.
     *
     * @param positionDTO the position to add
     * @return the added position with its new ID
     */
    public PositionDTO addOrderPosition(PositionDTO positionDTO) {
        String sql = "INSERT INTO position (produktnummer, auftragsnummer, menge) VALUES (?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, positionDTO.getProductNumber());
            pstmt.setInt(2, positionDTO.getOrderNumber());
            pstmt.setInt(3, positionDTO.getQuantity());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    positionDTO.setPositionNumber(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return positionDTO;
    }

    /**
     * Deletes a specific order position by its ID.
     *
     * @param positionId the ID of the position to delete
     */
    public void deleteOrderPosition(int positionId) {
        String sql = "DELETE FROM position WHERE positionsnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, positionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an entire order and its associated positions by order ID.
     *
     * @param orderId the ID of the order to delete
     */
    public void deleteOrder(int orderId) {
        String sqlOrder = "DELETE FROM auftrag WHERE auftragsnummer = ?";
        String sqlPosition = "DELETE FROM position WHERE auftragsnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmtPosition = conn.prepareStatement(sqlPosition);
             PreparedStatement pstmtOrder = conn.prepareStatement(sqlOrder)) {
            
            // Delete order positions first
            pstmtPosition.setInt(1, orderId);
            pstmtPosition.executeUpdate();
            
            // Then delete the order itself
            pstmtOrder.setInt(1, orderId);
            pstmtOrder.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Closes an order by checking product availability, adjusting stock levels, and updating the order status.
     * This method is transactional to ensure data consistency and integrity.
     * <p>
     * Steps:
     * 1. Checks the availability of all products in the order.
     * 2. If all products are available, reduces stock quantities and updates the order status to 'closed'.
     * 3. If not all products are available, performs a rollback to maintain data integrity.
     * <p>
     * This method uses JDBC connections and prepares statements for database interactions,
     * demonstrating how to handle transactions manually, including rollbacks in case of insufficient stock or errors.
     *
     * @param orderId The ID of the order to be closed.
     * @return true if the order is successfully closed, false otherwise.
     */
    @Transactional
    public boolean closeOrder(int orderId) {
        // Initialize JDBC objects
        Connection conn = null;
        PreparedStatement pstmtCheck = null;
        PreparedStatement pstmtUpdate = null;
        PreparedStatement pstmtCloseOrder = null;
        ResultSet rs = null;
        boolean allProductsAvailable = true;
        
        try {
            // Obtain a connection and disable auto-commit for transactional control
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Check the availability of products for all order positions
            String checkAvailabilitySql = "SELECT p.produktnummer, (l.menge - p.menge) AS available FROM position p " +
                                          "JOIN produktlagermenge l ON p.produktnummer = l.produkt_fk " +
                                          "WHERE p.auftragsnummer = ?";
            pstmtCheck = conn.prepareStatement(checkAvailabilitySql);
            pstmtCheck.setInt(1, orderId);
            rs = pstmtCheck.executeQuery();

            // Determine if all products are available
            while (rs.next()) {
                if (rs.getInt("available") < 0) {
                    allProductsAvailable = false;
                    break;
                }
            }

            if (allProductsAvailable) {
                // Reduce stock quantities for all order positions
                String updateStockSql = "UPDATE produktlagermenge l " +
                                        "JOIN position p ON l.produkt_fk = p.produktnummer " +
                                        "SET l.menge = l.menge - p.menge " +
                                        "WHERE p.auftragsnummer = ?";
                pstmtUpdate = conn.prepareStatement(updateStockSql);
                pstmtUpdate.setInt(1, orderId);
                pstmtUpdate.executeUpdate();

                // Update order status to 'closed'
                String closeOrderSql = "UPDATE auftrag SET status = 'geschlossen' WHERE auftragsnummer = ?";
                pstmtCloseOrder = conn.prepareStatement(closeOrderSql);
                pstmtCloseOrder.setInt(1, orderId);
                pstmtCloseOrder.executeUpdate();

                // Commit the transaction to finalize changes
                conn.commit();
                return true;
            } else {
                // Insufficient stock, perform a rollback
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            // Handle SQL exceptions by attempting to rollback the transaction
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            // Clean up JDBC resources in the finally block to ensure they are always closed
            try {
                if (rs != null) rs.close();
                if (pstmtCheck != null) pstmtCheck.close();
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (pstmtCloseOrder != null) pstmtCloseOrder.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Restore auto-commit mode
                    conn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
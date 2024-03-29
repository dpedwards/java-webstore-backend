/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import de.webstore.backend.dto.OrderDTO;
import de.webstore.backend.dto.PositionDTO;
import de.webstore.backend.exception.InsufficientStockException;
import de.webstore.backend.exception.OrderClosedException;
import de.webstore.backend.exception.OrderNotFoundException;
import de.webstore.backend.exception.PositionNotFoundException;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
                order.setOrderId(rs.getString("auftragsnummer"));
                order.setDate(rs.getDate("Datum").toLocalDate());
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orders;
    }

    /**
     * Finds a specific order by its ID and throws OrderNotFoundException if not found.
     *
     * @param orderId the order ID
     * @return the found order
     * @throws OrderNotFoundException if the order with the given ID is not found
     */
    public OrderDTO findById(String orderId) {
        OrderDTO order = null;
        String sql = "SELECT * FROM auftrag WHERE auftragsnummer = ?";

        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new OrderDTO();
                    order.setOrderId(rs.getString("auftragsnummer"));
                    order.setStatus(rs.getString("status"));
                    order.setDate(rs.getDate("datum").toLocalDate());
                } else {
                    // If the order with the specified ID is not found, throw OrderNotFoundException
                    throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Consider wrapping and rethrowing SQLException as a runtime exception or a custom checked exception
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
        // Generate a new UUID for the order
        String uuid = UUID.randomUUID().toString();
        orderDTO.setOrderId(uuid); // Set autogenerated UUID as order ID
        String sql = "INSERT INTO auftrag (auftragsnummer, datum, status) VALUES (?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, uuid);
            pstmt.setDate(2, java.sql.Date.valueOf(orderDTO.getDate()));
            pstmt.setString(3, "offen");  // status is always 'offen' when creating a new order
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    orderDTO.setOrderId(generatedKeys.getString(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return orderDTO;
    }

    /**
     * Adds a position to an existing order if the order's status is not "geschlossen" (closed).
     * Additionally, checks if the specified quantity of the product is available in stock before adding.
     * If the requested quantity exceeds available stock, it still adds the position but returns a message indicating the maximum available quantity.
     *
     * @param orderId      The ID of the order to add the position to.
     * @param positionDTO  The position data to add, including product ID and quantity.
     * @return The added position with its new ID and possibly a message about the available quantity.
     * @throws OrderClosedException if the order is closed.
     */
    public PositionDTO addOrderPosition(String orderId, PositionDTO positionDTO) throws OrderClosedException {
        // Ensure the order is open and exists
        if (!checkOrderExistsAndOpen(orderId)) {
            throw new OrderClosedException("Order with ID " + orderId + " is closed or does not exist.");
        }

        // Set orderId for the position
        positionDTO.setOrderId(orderId);

        // Assign a new UUID for the position
        String uuid = UUID.randomUUID().toString();
        positionDTO.setPositionId(uuid); // Update the positionDTO with the new position ID

        // Calculate available quantity for the product
        int availableQuantity = calculateAvailableProductQuantity(positionDTO.getProductId());
        
        // Check if requested quantity is available
        if (positionDTO.getQuantity() > availableQuantity) {
            // Modify the quantity to the available quantity if requested quantity exceeds availability
            //positionDTO.setQuantity(availableQuantity);
            // Optionally, add a message to positionDTO about adjusted quantity; assuming PositionDTO has a method to set messages
            //positionDTO.setMessage("Requested quantity exceeds available stock. Adjusted to available quantity: " + availableQuantity);
            System.out.println("");
            System.out.println("##########################");
            System.out.println("http://localhost:8080/api/de/v1/order/" + orderId + "/add/new/position");
            System.out.println("Requested quantity exceeds available stock. Adjusted to the available quantity, the maximum available quantities are: " + availableQuantity);
            System.out.println("##########################");
            System.out.println("");
        }

        // Proceed to add the position with either requested or adjusted quantity
        String sql = "INSERT INTO position (positionsnummer, produktnummer, auftragsnummer, menge) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, positionDTO.getProductId());
            pstmt.setString(3, positionDTO.getOrderId());
            pstmt.setInt(4, positionDTO.getQuantity());
            pstmt.executeUpdate();

            // Successfully added the position, return the PositionDTO (including any message about adjusted quantity)
            return positionDTO;
        } catch (SQLException e) {
            System.out.println("Error adding order position: " + e.getMessage());
            // In a real scenario, consider logging this error and potentially throwing a custom exception
            return null;
        }
    }

    /**
     * Calculates the total available quantity of a product across all warehouses.
     *
     * @param productId The ID of the product to calculate the quantity for.
     * @return The total available quantity of the product.
     */
    private int calculateAvailableProductQuantity(String productId) {
        String sql = "SELECT SUM(menge) AS availableQuantity FROM produktlagermenge WHERE produkt_fk = ?";
        try (Connection conn = databaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("availableQuantity");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating available product quantity: " + e.getMessage());
            // In a real scenario, consider logging this error and potentially throwing a custom exception
        }
        return 0; // Return 0 if product is not found or in case of error
    }

    /**
     * Deletes a specific order position by its ID.
     *
     * @param positionId the ID of the position to delete
     */
    public void deleteOrderPosition(String positionId) {
        String sql = "DELETE FROM position WHERE positionsnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, positionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an entire order and its associated positions by order ID.
     *
     * @param orderId the ID of the order to delete
     * @throws OrderClosedException 
     */
    @SuppressWarnings("deprecation")
    public void deleteOrder(String orderId) throws OrderClosedException {
        // Check if the order is closed
        String statusSql = "SELECT status FROM auftrag WHERE auftragsnummer = ?";
        String status = jdbcTemplate.queryForObject(statusSql, new Object[]{orderId}, String.class);
        if ("geschlossen".equals(status)) {
            throw new OrderClosedException("Order with ID " + orderId + " is closed and cannot be deleted.");
        }

        // Delete associated positions
        String sqlPosition = "DELETE FROM position WHERE auftragsnummer = ?";
        jdbcTemplate.update(sqlPosition, orderId);

        // Delete the order
        String sqlOrder = "DELETE FROM auftrag WHERE auftragsnummer = ?";
        jdbcTemplate.update(sqlOrder, orderId);
    }

    /**
     * Attempts to close an order by verifying if sufficient stock exists across warehouses for each product
     * in the order and deducting the necessary quantities if possible.
     *
     * @param orderId The ID of the order to close.
     * @return true if the order was successfully closed, false otherwise.
     * @throws OrderNotFoundException if the order ID does not exist.
     * @throws InsufficientStockException if there isn't enough stock to fulfill the order.
     */
    public boolean closeOrder(String orderId) throws OrderNotFoundException, InsufficientStockException {
        Connection conn = null;
        try {
            conn = databaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Verify if the order exists and is not already closed
            if (!isOrderOpen(conn, orderId)) {
                throw new OrderNotFoundException("Order not found or already closed: " + orderId);
            }
            
            // Check and prepare stock deduction for each product in the order
            Map<String, Integer> stockDeductions = prepareStockDeductions(conn, orderId);
            
            // Deduct stock from 'produktlagermenge' for each product
            for (Map.Entry<String, Integer> entry : stockDeductions.entrySet()) {
                deductStockForProductAndUpdateLager(conn, entry.getKey(), entry.getValue());
            }
            
            // Close the order
            closeOrderInDatabase(conn, orderId);
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
    /**
     * Checks if a given order exists in the database and if its status is "open".
     * <p>
     * This method queries the database to verify the existence of an order by its ID. It then checks
     * if the status of the order is "open", indicating that it has not yet been closed and is
     * eligible for further modifications or processing.
     * <p>
     * @param conn The {@link Connection} object representing an active connection to the database.
     *             This connection is used to execute the query to check the order's status.
     * @param orderId The unique identifier of the order to be checked. This ID is used in the query
     *                to fetch the specific order from the database.
     * @return {@code true} if the order exists and its status is "open", {@code false} otherwise.
     *         If the order does not exist, or if it exists but its status is not "open", this method
     *         returns {@code false}.
     * @throws SQLException If an SQL error occurs while executing the query. This exception is thrown
     *         if there are issues communicating with the database, such as syntax errors in the SQL
     *         query, problems with the database connection, or other related errors.
     */
    private boolean isOrderOpen(Connection conn, String orderId) throws SQLException {
        // SQL query to check if the order exists and its status is "open"
        String query = "SELECT COUNT(*) FROM auftrag WHERE auftragsnummer = ? AND status = 'offen'";
        
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // If the query returns a count greater than 0, the order is open
                    return rs.getInt(1) > 0;
                }
            }
        }
        // If the order does not exist or is not open, return false
        return false;
    }

    /**
     * Prepares a map of stock deductions required for each product in an order based on the product's ID.
     * This method calculates the total quantity needed for each product in the order by comparing it against
     * the available stock quantities in the 'produktlagermenge' table.
     * <p>
     * The method iterates through each product in the order, sums up the required quantity (from the 'position' table),
     * and checks it against the available stock in the 'produktlagermenge'. If the required quantity exceeds the available
     * stock for any product, an {@link InsufficientStockException} is thrown, indicating that the order cannot be fulfilled.
     * 
     * @param conn    The {@link Connection} object representing an active connection to the database. This connection is
     *                used to execute SQL queries to fetch order details and check stock availability.
     * @param orderId The unique identifier of the order for which stock deductions are being prepared.
     * @return A {@link Map} where each key is a product ID (as {@link String}) and each value is the integer quantity of stock
     *         to be deducted.
     * @throws SQLException If an SQL error occurs while executing queries. This exception indicates issues with the database
     *         communication, such as syntax errors in the SQL, problems establishing a connection to the database, etc.
     * @throws InsufficientStockException If there is not enough stock available to fulfill the order. This exception includes
     *         details about the product(s) for which the available stock is insufficient to meet the order requirements.
     */
    private Map<String, Integer> prepareStockDeductions(Connection conn, String orderId) throws SQLException, InsufficientStockException {
        Map<String, Integer> deductions = new HashMap<>();
        
        // SQL to get total required quantity for each product in the order
        String requiredQuantitySql = """
            SELECT p.produktnummer, SUM(p.menge) AS requiredQuantity
            FROM position p
            WHERE p.auftragsnummer = ?
            GROUP BY p.produktnummer""";
        
        // SQL to get available stock for each product
        String availableStockSql = """
            SELECT produkt_fk, SUM(menge) AS availableQuantity
            FROM produktlagermenge
            WHERE produkt_fk = ?""";
        
        try (PreparedStatement requiredStmt = conn.prepareStatement(requiredQuantitySql)) {
            requiredStmt.setString(1, orderId);
            try (ResultSet requiredRs = requiredStmt.executeQuery()) {
                while (requiredRs.next()) {
                    String productId = requiredRs.getString("produktnummer");
                    int requiredQuantity = requiredRs.getInt("requiredQuantity");

                    try (PreparedStatement availableStmt = conn.prepareStatement(availableStockSql)) {
                        availableStmt.setString(1, productId);
                        try (ResultSet availableRs = availableStmt.executeQuery()) {
                            if (availableRs.next()) {
                                int availableQuantity = availableRs.getInt("availableQuantity");
                                if (requiredQuantity > availableQuantity) {
                                    throw new InsufficientStockException("Insufficient stock for product ID: " + productId);
                                }
                                // Prepare deduction amount
                                deductions.put(productId, requiredQuantity);
                            } else {
                                throw new InsufficientStockException("Product ID: " + productId + " not found in stock.");
                            }
                        }
                    }
                }
            }
        }
        return deductions;
    }

    /**
     * Deducts a specified quantity of stock for a given product from the 'produktlagermenge' table
     * and updates the 'lager' table with the new total quantities.
     * 
     * This method ensures that the stock levels are accurately adjusted in the 'produktlagermenge' table
     * for the specified product across all warehouses. After deducting the specified quantity from the product's stock,
     * it calculates the new total stock quantities for each warehouse where the product is stored and updates the 'lager' table accordingly.
     * 
     * @param conn The database connection object used for executing SQL statements.
     * @param productId The unique identifier of the product for which the stock is to be deducted.
     * @param quantity The amount of stock to be deducted from the product's total stock across all warehouses.
     * @throws SQLException If an error occurs during the database update operations.
     * @throws InsufficientStockException If there is not enough stock available for the product to deduct the specified quantity.
     */
    private void deductStockForProductAndUpdateLager(Connection conn, String productId, Integer quantity) throws SQLException, InsufficientStockException {
        // SQL query to deduct stock for the specified product ID from 'produktlagermenge'
        String sql = """
            UPDATE produktlagermenge
            SET menge = GREATEST(0, menge - ?)
            WHERE produkt_fk = ?
            """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setString(2, productId);
            
            // Execute the update operation
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                // If no rows were affected, it indicates that the product does not exist in 'produktlagermenge'
                throw new InsufficientStockException("No stock found for product ID: " + productId + " to deduct.");
            }
            
            // Step 1: Calculate the new total quantities for each warehouse associated with the product
            String queryTotalQuantities = """
                SELECT lager_fk, SUM(menge) AS totalQuantity
                FROM produktlagermenge
                WHERE produkt_fk = ?
                GROUP BY lager_fk
            """;

            // Step 2: Prepare to update the 'lager' table with the calculated new totals
            String updateLager = """
                UPDATE lager
                SET menge = ?
                WHERE lagernummer = ?
            """;

            try (
                PreparedStatement pstmtQueryTotals = conn.prepareStatement(queryTotalQuantities);
                PreparedStatement pstmtUpdateLager = conn.prepareStatement(updateLager)
            ) {
                pstmtQueryTotals.setString(1, productId);
                ResultSet rs = pstmtQueryTotals.executeQuery();
                
                // Iterate through the results to update each warehouse's stock total in 'lager'
                while (rs.next()) {
                    int lagerFk = rs.getInt("lager_fk");
                    int totalQuantity = rs.getInt("totalQuantity");

                    // Update the 'lager' table with the new total quantity for the warehouse
                    pstmtUpdateLager.setInt(1, totalQuantity);
                    pstmtUpdateLager.setInt(2, lagerFk);
                    pstmtUpdateLager.executeUpdate();
                }
            }
        }
    }

    /**
     * Updates the status of an order to 'closed' in the database.
     * <p>
     * This method marks an order as closed by updating its status in the 'auftrag' table. It is a crucial
     * step in the process of closing an order, signifying that the order has been fully processed and
     * no further modifications to it are allowed. This change is committed to the database to reflect
     * the final state of the order.
     * <p>
     * The method uses a JDBC {@link Connection} to execute an SQL update command that sets the order's
     * status to 'geschlossen' for the specified order ID. It assumes that the connection is already
     * open and managed externally, allowing for transaction control and potential rollback in case
     * of errors or business logic requirements not being met.
     *
     * @param conn    The {@link Connection} object representing an active connection to the database.
     *                This connection is used to execute the update operation on the 'auftrag' table.
     * @param orderId The unique identifier of the order to be closed. This ID is used to locate the
     *                specific order record in the 'auftrag' table.
     * @throws SQLException If an SQL error occurs during the execution of the update operation. This
     *                      exception signals issues such as problems with the SQL syntax, failure in
     *                      establishing a connection to the database, or issues in updating the record.
     */
    private void closeOrderInDatabase(Connection conn, String orderId) throws SQLException {
        // SQL query to update the order status to 'closed'
        String sql = "UPDATE auftrag SET status = 'geschlossen' WHERE auftragsnummer = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, orderId);
            
            // Execute the update
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                // If no rows were affected, it implies that the order ID does not exist in the database
                throw new OrderNotFoundException("Failed to close order. Order ID " + orderId + " not found.");
            }
        }
    }

    /**
     * Checks if an order exists in the database.
     * 
     * @param orderId The ID of the order to check.
     * @return true if the order exists, false otherwise.
     */
    public boolean checkOrderExists(String orderId) {
        String sql = "SELECT COUNT(*) FROM auftrag WHERE auftragsnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking order existence: " + e.getMessage());
        }
        return false;
    }

    /**
     * Checks if an order exists and is open.
     *
     * @param orderId The ID of the order.
     * @return True if the order exists and is open, false otherwise.
     * @throws OrderNotFoundException if the order does not exist.
     * @throws OrderClosedException if the order is closed.
     */
    public boolean checkOrderExistsAndOpen(String orderId) throws OrderNotFoundException, OrderClosedException {
        String sql = "SELECT status FROM auftrag WHERE auftragsnummer = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String status = rs.getString("status");
                if ("geschlossen".equals(status)) {
                    throw new OrderClosedException("Order with ID " + orderId + " is closed.");
                }
                return true; // Order exists and is open
            } else {
                throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            // Consider re-throwing as a runtime exception or specific checked exception as needed
            throw new RuntimeException("Database error occurred while checking order status.", e);
        }
    }

    /**
     * Deletes a specific position from an order only if the order's status is "offen" (open).
     *
     * @param orderId    The ID of the order from which to delete the position.
     * @param positionId The ID of the position to delete.
     * @throws Exception 
     * @throws OrderClosedException If the order's status is "geschlossen" (closed).
     * @throws ResponseStatusException With {@code HttpStatus.NOT_FOUND} if either the position does not exist
     *         or it does not belong to the specified order.
     */
    @SuppressWarnings("deprecation")
    public void deleteOrderPosition(String orderId, String positionId) throws OrderClosedException {
        // Check if the order is open
        try {
            String orderStatusSql = "SELECT status FROM auftrag WHERE auftragsnummer = ?";
            String status = jdbcTemplate.queryForObject(orderStatusSql, new Object[]{orderId}, String.class);
            if ("geschlossen".equals(status)) {
                throw new OrderClosedException("Order with ID " + orderId + " is closed.");
            }
        } catch (EmptyResultDataAccessException e) {
            throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
        }

        // Verify if the position belongs to the given order
        String verifySql = "SELECT COUNT(*) FROM position WHERE positionsnummer = ? AND auftragsnummer = ?";
        int count = jdbcTemplate.queryForObject(verifySql, new Object[]{positionId, orderId}, Integer.class);
        if (count == 0) {
            throw new PositionNotFoundException("Position not found or does not belong to the specified order.");
        }

        // Delete the position
        String deleteSql = "DELETE FROM position WHERE positionsnummer = ?";
        int affectedRows = jdbcTemplate.update(deleteSql, positionId);
        if (affectedRows == 0) {
            // This should not occur due to the checks above
            throw new PositionNotFoundException("Failed to delete the position.");
        }
    }
}
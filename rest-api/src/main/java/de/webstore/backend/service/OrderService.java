package de.webstore.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.webstore.backend.dto.OrderDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.webstore.backend.config.DatabaseConnection;

@Service
public class OrderService {

    private final DatabaseConnection databaseConnection;

    @Autowired
    public OrderService(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<OrderDTO> findAll() {
        List<OrderDTO> orders = new ArrayList<>();
        String sql = "SELECT * FROM auftrag";

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

    public OrderDTO findById(int id) {
        OrderDTO order = null;
        String sql = "SELECT * FROM auftrag WHERE auftragsnummer = ?";

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    order = new OrderDTO();
                    order.setOrderNumber(rs.getInt("auftragsnummer")); // Fix: Assign the value to order.setOrderNumber()
                    order.setDate(rs.getDate("datum").toLocalDate());
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return order;
    }

    public OrderDTO create(OrderDTO OrderDTO) {
        String sql = "INSERT INTO auftrag(datum) VALUES(?)";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(OrderDTO.getDate()));
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    OrderDTO.setOrderNumber(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return OrderDTO;
    }

    public OrderDTO update(int id, OrderDTO OrderDTO) {
        String sql = "UPDATE auftrag SET datum = ? WHERE auftragsnummer = ?";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, java.sql.Date.valueOf(OrderDTO.getDate()));
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
        
        return OrderDTO;
    }

    public void delete(int id) {
        String sql = "DELETE FROM auftrag WHERE auftragsnummer = ?";
        
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

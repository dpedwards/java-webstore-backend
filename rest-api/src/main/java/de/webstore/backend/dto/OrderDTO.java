/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) for Order entities.
 * <p>
 * This class is used to transfer order data between processes or through network calls without exposing domain models.
 */
@Schema(description = "Data Transfer Object for Order")
public class OrderDTO {
    
    // Order ID uniquely identifying the order
    @Schema(hidden = true) // This hides the orderId property in Swagger UI
    private String orderId;
    
    // Date on which the order was placed
    //@Schema(description = "Date the order was placed", example = "2024-03-05")
    private LocalDate date;

    // Status from the order
    @Schema(description = "Order status", example = "offen")
    private String status;

    /**
     * Default constructor.
     */
    public OrderDTO() {}

    /**
     * Parameterized constructor to create an order DTO with a specific order ID and date.
     *
     * @param orderId the unique order ID
     * @param date the date the order was placed
     */
    public OrderDTO(String orderId, LocalDate date, String status) {
        this.orderId = orderId;
        this.date = date;
        this.status = status;
    }

    /**
     * Gets the order ID.
     *
     * @return the order ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Gets the date the order was placed.
     *
     * @return the order date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the status current status from the order.
     *
     * @return the order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the order ID.
     *
     * @param orderId the order ID to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Sets the date the order was placed.
     *
     * @param localDate the order date to set
     */
    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

    /**
     * Sets the date the order was placed.
     *
     * @param status from the order to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the OrderDTO.
     *
     * @return a string representation of the OrderDTO
     */
    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderId=" + orderId +
                "status=" + status + 
                ", date=" + date +
                '}';
    }
}

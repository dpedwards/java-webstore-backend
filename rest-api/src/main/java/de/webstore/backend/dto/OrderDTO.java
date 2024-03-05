/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for Order entities.
 * <p>
 * This class is used to transfer order data between processes or through network calls without exposing domain models.
 */
public class OrderDTO {
    
    // Order number uniquely identifying the order
    private int orderNumber;
    
    // Date on which the order was placed
    private LocalDate date;

    /**
     * Default constructor.
     */
    public OrderDTO() {}

    /**
     * Parameterized constructor to create an order DTO with a specific order number and date.
     *
     * @param orderNumber the unique order number
     * @param date the date the order was placed
     */
    public OrderDTO(int orderNumber, LocalDate date) {
        this.orderNumber = orderNumber;
        this.date = date;
    }

    /**
     * Gets the order number.
     *
     * @return the order number
     */
    public int getOrderNumber() {
        return orderNumber;
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
     * Sets the order number.
     *
     * @param orderNumber the order number to set
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
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
     * Returns a string representation of the OrderDTO.
     *
     * @return a string representation of the OrderDTO
     */
    @Override
    public String toString() {
        return "OrderDTO{" +
                "orderNumber=" + orderNumber +
                ", date=" + date +
                '}';
    }
}

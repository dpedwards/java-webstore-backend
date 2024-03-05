/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.model;

import java.sql.Date;

/**
 * Represents an order entity in the system.
 * <p>
 * This class models the order entity with its attributes and provides getter and setter methods to manipulate its state.
 */
public class Order {
    
    // Unique identifier for the order
    private int orderNumber;
    
    // The date the order was placed
    private Date date;

    /**
     * Retrieves the order number.
     *
     * @return the order number
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order number.
     *
     * @param orderNumber the new order number
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Retrieves the date the order was placed.
     *
     * @return the order date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date the order was placed.
     *
     * @param date the new order date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Returns a string representation of the Order.
     * <p>
     * Provides a human-readable form of the order entity, including its order number and date.
     *
     * @return a string detailing the order's information
     */
    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", date=" + date +
                '}';
    }
}
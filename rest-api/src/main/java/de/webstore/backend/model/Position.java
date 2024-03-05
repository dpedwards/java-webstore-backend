/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.model;

/**
 * Represents a position within an order in the system.
 * <p>
 * This class models the position entity with attributes such as position number, product number, order number, and quantity. 
 * It provides getter and setter methods to manipulate its state and a toString method for a readable representation.
 */
public class Position {

    // Unique identifier for the position
    private int positionNumber;
    
    // Product number associated with this position
    private int productNumber;
    
    // Order number to which this position belongs
    private int orderNumber;
    
    // Quantity of the product in this position
    private int quantity;

    /**
     * Retrieves the position number.
     *
     * @return the position number
     */
    public int getPositionNumber() {
        return positionNumber;
    }

    /**
     * Sets the position number.
     *
     * @param positionNumber the new position number
     */
    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    /**
     * Retrieves the product number associated with this position.
     *
     * @return the product number
     */
    public int getProductNumber() {
        return productNumber;
    }

    /**
     * Sets the product number for this position.
     *
     * @param productNumber the new product number
     */
    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    /**
     * Retrieves the order number to which this position belongs.
     *
     * @return the order number
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order number for this position.
     *
     * @param orderNumber the new order number
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Retrieves the quantity of the product in this position.
     *
     * @return the quantity of the product
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the product for this position.
     *
     * @param quantity the new quantity of the product
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns a string representation of the Position.
     * <p>
     * Provides a human-readable form of the position entity, including its position number, product number, order number, and quantity.
     *
     * @return a string detailing the position's information
     */
    @Override
    public String toString() {
        return "Position{" +
                "positionNumber=" + positionNumber +
                ", productNumber=" + productNumber +
                ", orderNumber=" + orderNumber +
                ", quantity=" + quantity +
                '}';
    }
}
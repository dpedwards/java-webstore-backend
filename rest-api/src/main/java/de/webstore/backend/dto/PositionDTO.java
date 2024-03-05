/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

/**
 * Data Transfer Object (DTO) for Position entities.
 * <p>
 * This class is used to transfer position data related to orders and products between processes or through network calls without exposing domain models.
 */
public class PositionDTO {
    
    // Unique identifier for the position
    private int positionNumber;
    
    // Product number associated with this position
    private int productNumber;
    
    // Order number associated with this position
    private int orderNumber;
    
    // Quantity of the product in this position
    private int quantity;

    /**
     * Default constructor.
     */
    public PositionDTO() {
    }

    /**
     * Constructs a PositionDTO with specified details.
     *
     * @param positionNumber Unique identifier for the position
     * @param productNumber Product number associated with this position
     * @param orderNumber Order number associated with this position
     * @param quantity Quantity of the product in this position
     */
    public PositionDTO(int positionNumber, int productNumber, int orderNumber, int quantity) {
        this.positionNumber = positionNumber;
        this.productNumber = productNumber;
        this.orderNumber = orderNumber;
        this.quantity = quantity;
    }

    // Getters and Setters
    
    /**
     * Gets the position number.
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
     * Gets the product number.
     *
     * @return the product number
     */
    public int getProductNumber() {
        return productNumber;
    }

    /**
     * Sets the product number.
     *
     * @param productNumber the new product number
     */
    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
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
     * Sets the order number.
     *
     * @param orderNumber the new order number
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity the new quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns a string representation of the PositionDTO.
     *
     * @return a string detailing the position's information
     */
    @Override
    public String toString() {
        return "PositionDTO{" +
                "positionNumber=" + positionNumber +
                ", productNumber=" + productNumber +
                ", orderNumber=" + orderNumber +
                ", quantity=" + quantity +
                '}';
    }
}
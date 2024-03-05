/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) for Position entities.
 * <p>
 * This class is used to transfer position data related to orders and products between processes or through network calls without exposing domain models.
 */
@Schema(description = "Data Transfer Object for Position")
public class PositionDTO {
    
    // Unique identifier for the position
    @Schema(hidden = true) // This hides the positionId property in Swagger UI
    private String positionId;
    
    // Product ID associated with this position
    private String productId;
    
    // Order ID associated with this position
    //@Schema(hidden = true) // This hides the orderId property in Swagger UI
    private String orderId;
    
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
     * @param positionId Unique identifier for the position
     * @param productId Product ID associated with this position
     * @param orderId Order ID associated with this position
     * @param quantity Quantity of the product in this position
     */
    public PositionDTO(String positionId, String productId, String orderId, int quantity) {
        this.positionId = positionId;
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    // Getters and Setters
    
    /**
     * Gets the position ID.
     *
     * @return the position ID
     */
    public String getPositionId() {
        return positionId;
    }

    /**
     * Sets the position ID.
     *
     * @param positionId the new position ID
     */
    public void setPositionNumber(String positionId) {
        this.positionId = positionId;
    }

    /**
     * Gets the product ID.
     *
     * @return the product ID
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Sets the product ID.
     *
     * @param productId the new product ID
     */
    public void setProductId(String productId) {
        this.productId = productId;
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
     * Sets the order ID.
     *
     * @param orderId the new order ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
                "positionId=" + positionId +
                ", productId=" + productId +
                ", orderId=" + orderId +
                ", quantity=" + quantity +
                '}';
    }
}
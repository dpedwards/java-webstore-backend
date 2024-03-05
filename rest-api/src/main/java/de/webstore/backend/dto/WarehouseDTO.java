/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) for Warehouse entities.
 * <p>
 * This class is used to transfer warehouse data, including warehouse Number and quantity of products,
 * between processes or through network calls without exposing domain models.
 */
public class WarehouseDTO {
    
    // Number for the warehouse
    @Schema(description = "Number for the warehouse", example = "1")
    private int warehouseNumber;
    
    // Quantity of products stored in the warehouse
    @Schema(description = "Quantity of products stored in the warehouse", example = "500")
    private int quantity;

    // Status of warehouse if active or not
    @Schema(description = "Status of warehouse if active or not", example = "TRUE")
    private boolean isActive;

    /**
     * Default constructor for creating an empty WarehouseDTO instance.
     */
    public WarehouseDTO() {}

    /**
     * Constructs a WarehouseDTO with a specified warehouse ID and quantity.
     *
     * @param warehouseNumber the unique identifier of the warehouse
     * @param quantity the quantity of products in the warehouse
     * @param isActive the status of the warehouse
     */
    public WarehouseDTO(int warehouseNumber, int quantity, boolean isActive) {
        this.warehouseNumber = warehouseNumber;
        this.quantity = quantity;
        this.isActive = isActive;
    }

    // Getters
    
    /**
     * Retrieves the warehouse Number.
     *
     * @return the number of the warehouse
     */
    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    /**
     * Retrieves the quantity of products in the warehouse.
     *
     * @return the quantity of products
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Retrieves the status of the warehouse.
     * @return the status of the warehouse
     */
    public boolean getIsActive() {
        return isActive;
    }

    // Setters
    
    /**
     * Sets the warehouse Number.
     *
     * @param warehouseNumber the new number for the warehouse
     */
    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    /**
     * Sets the quantity of products in the warehouse.
     *
     * @param quantity the new quantity of products
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Sets the status of the warehouse.
     * 
     * @param isActive the new status of the warehouse
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * Returns a string representation of the WarehouseDTO.
     * <p>
     * This method provides a human-readable form of the warehouse data.
     *
     * @return a string detailing the warehouse's information
     */
    @Override
    public String toString() {
        return "WarehouseDTO{" +
                "warehouseNumber=" + warehouseNumber +
                ", quantity=" + quantity +
                '}';
    }
}
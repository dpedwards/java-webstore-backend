/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

/**
 * Data Transfer Object (DTO) for Warehouse entities.
 * <p>
 * This class is used to transfer warehouse data, including warehouse number and quantity of products,
 * between processes or through network calls without exposing domain models.
 */
public class WarehouseDTO {
    
    // Unique identifier for the warehouse
    private int warehouseNumber;
    
    // Quantity of products stored in the warehouse
    private int quantity;

    /**
     * Default constructor for creating an empty WarehouseDTO instance.
     */
    public WarehouseDTO() {}

    /**
     * Constructs a WarehouseDTO with a specified warehouse number and quantity.
     *
     * @param warehouseNumber the unique identifier of the warehouse
     * @param quantity the quantity of products in the warehouse
     */
    public WarehouseDTO(int warehouseNumber, int quantity) {
        this.warehouseNumber = warehouseNumber;
        this.quantity = quantity;
    }

    // Getters
    
    /**
     * Retrieves the warehouse number.
     *
     * @return the unique identifier of the warehouse
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

    // Setters
    
    /**
     * Sets the warehouse number.
     *
     * @param warehouseNumber the new unique identifier for the warehouse
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
/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.model;

/**
 * Represents a warehouse entity in the system.
 * <p>
 * This class models the warehouse entity with its attributes, including a unique warehouse number and quantity of products stored. 
 * It provides getter and setter methods for manipulating its state and a toString method for logging purposes.
 */
public class Warehouse {

    // Unique identifier for the warehouse
    private int warehouseNumber;
    
    // Quantity of products stored in the warehouse
    private int quantity;

    /**
     * Retrieves the warehouse number.
     * <p>
     * This method returns the unique identifier of the warehouse.
     *
     * @return the warehouse number
     */
    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    /**
     * Sets the warehouse number.
     * <p>
     * This method updates the unique identifier of the warehouse.
     *
     * @param warehouseNumber the new warehouse number
     */
    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    /**
     * Retrieves the quantity of products in the warehouse.
     * <p>
     * This method returns the total number of products stored in the warehouse.
     *
     * @return the quantity of products
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of products in the warehouse.
     * <p>
     * This method updates the total number of products stored in the warehouse.
     *
     * @param quantity the new quantity of products
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns a string representation of the Warehouse.
     * <p>
     * Provides a human-readable form of the warehouse entity, including its warehouse number and quantity of products stored.
     *
     * @return a string detailing the warehouse's information
     */
    @Override
    public String toString() {
        return "Warehouse{" +
                "warehouseNumber=" + warehouseNumber +
                ", quantity=" + quantity +
                '}';
    }
}
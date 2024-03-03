package de.webstore.backend.dto;

public class WarehouseDTO {
    
    private int warehouseNumber;
    private int quantity;

    // Constructors
    public WarehouseDTO() {}

    public WarehouseDTO(int warehouseNumber, int quantity) {
        this.warehouseNumber = warehouseNumber;
        this.quantity = quantity;
    }

    // Getter
    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    public int getQuanity() {
        return quantity;
    }

    // Setter
    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString
    @Override
    public String toString() {
        return "LagerDTO{" +
                "Lagernummer=" + warehouseNumber +
                ", Menge=" + quantity +
                '}';
    }
}
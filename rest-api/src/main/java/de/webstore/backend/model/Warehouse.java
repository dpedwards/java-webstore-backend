package de.webstore.backend.model;

public class Warehouse {

    private int warehouseNumber;
    private int quantity;

    // Getter und Setter
    public int getWarehouseNumber() {
        return warehouseNumber;
    }

    public void setWarehouseNumber(int warehouseNumber) {
        this.warehouseNumber = warehouseNumber;
    }

    public int getMenge() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString-Methode
    @Override
    public String toString() {
        return "Lager{" +
                "Lagernummer=" + warehouseNumber +
                ", Menge=" + quantity +
                '}';
    }
}

package de.webstore.backend.model;

public class Position {

    private int positionNumber;
    private int productNumber;
    private int orderNumber;
    private int quantity;

    // Getter und Setter
    public int getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getQuanity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString-Methode
    @Override
    public String toString() {
        return "Position{" +
                "Positionsnummer=" + positionNumber +
                ", Produktnummer=" + productNumber +
                ", Auftragsnummer=" + orderNumber +
                ", Menge=" + quantity +
                '}';
    }
}

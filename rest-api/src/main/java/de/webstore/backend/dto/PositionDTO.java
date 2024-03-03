package de.webstore.backend.dto;

public class PositionDTO {
    
    private int positionNumber;
    private int productNumber;
    private int orderNumber;
    private int quantity;

    // Constructoren
    public PositionDTO() {}

    public PositionDTO(int positionNumber, int productNumber, int orderNumber, int quantity) {
        this.positionNumber = positionNumber;
        this.productNumber = productNumber;
        this.orderNumber = orderNumber;
        this.quantity = quantity;
    }

    // Getter
    public int getPositionNumber() {
        return positionNumber;
    }

    public int getProductNumnber() {
        return productNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setter
    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // toString
    @Override
    public String toString() {
        return "PositionDTO{" +
                "Positionsnummer=" + positionNumber +
                ", Produktnummer=" + productNumber +
                ", Auftragsnummer=" + orderNumber +
                ", Menge=" + quantity +
                '}';
    }
}
package de.webstore.backend.dto;

import java.math.BigDecimal;

public class ProductDTO {
    
    private int productNumber;
    private String name;
    private String unit;
    private BigDecimal price;

    // Constructors
    public ProductDTO() {}

    public ProductDTO(int productNumber, String name, String unit, BigDecimal price) {
        this.productNumber = productNumber;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }

    // Getter
    public int getProductNumber() {
        return productNumber;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Setter
    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    // toString
    @Override
    public String toString() {
        return "ProduktDTO{" +
                "Produktnummer=" + productNumber +
                ", Name='" + name + '\'' +
                ", Unit='" + unit + '\'' +
                ", Price=" + price +
                '}';
    }
}
package de.webstore.backend.model;

public class Product {
    
    private int productNumber;
    private String name;
    private String unit;
    private Double price;

    // Standard construktor
    public Product() {
    }

    // Constructorr with all fields
    public Product(int productNumber, String name, String unit, Double price) {
        this.productNumber = productNumber;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }

    // Getter und Setter
    public int getProductNumber() {
        return productNumber;
    }

    public void setId(int productNumber) {
        this.productNumber = productNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    // toString-Methode für Logging
    @Override
    public String toString() {
        return "Produkt{" +
                "Produktnummer=" + productNumber +
                ", Name='" + name + '\'' +
                ", Einheit='" + unit + '\'' +
                ", Preis=" + price +
                '}';
    }
}

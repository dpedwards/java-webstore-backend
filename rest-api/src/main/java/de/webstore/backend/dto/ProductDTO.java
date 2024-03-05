/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for Product entities.
 * <p>
 * This class is used to transfer product data between processes or through network calls without exposing domain models.
 */
public class ProductDTO {
    
    // Unique product identifier
    private int productNumber;
    
    // Name of the product
    private String name;
    
    // Measurement unit of the product (e.g., "kg", "liters")
    private String unit;
    
    // Price of the product
    private BigDecimal price;

    /**
     * Default constructor.
     */
    public ProductDTO() {}

    /**
     * Constructs a ProductDTO with specified details.
     *
     * @param productNumber Unique identifier for the product
     * @param name Name of the product
     * @param unit Measurement unit of the product
     * @param price Price of the product
     */
    public ProductDTO(int productNumber, String name, String unit, BigDecimal price) {
        this.productNumber = productNumber;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }

    // Getters
    
    /**
     * Gets the product number.
     *
     * @return the product number
     */
    public int getProductNumber() {
        return productNumber;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name of the product
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the unit of measure for the product.
     *
     * @return the unit of measure
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price of the product
     */
    public BigDecimal getPrice() {
        return price;
    }

    // Setters
    
    /**
     * Sets the product number.
     *
     * @param productNumber the new product number
     */
    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    /**
     * Sets the name of the product.
     *
     * @param name the new name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the unit of measure for the product.
     *
     * @param unit the new unit of measure
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the new price of the product
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the ProductDTO.
     *
     * @return a string detailing the product's information
     */
    @Override
    public String toString() {
        return "ProductDTO{" +
                "productNumber=" + productNumber +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                '}';
    }
}
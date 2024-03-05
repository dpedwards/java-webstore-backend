/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.model;

/**
 * Represents a product entity in the system.
 * <p>
 * This class models the product entity with its attributes, including product number, name, unit, and price. 
 * It provides constructor methods for instantiation and getter/setter methods for manipulating its state.
 */
public class Product {
    
    // Unique identifier for the product
    private int productNumber;
    
    // Name of the product
    private String name;
    
    // Unit of measurement for the product (e.g., "kg", "litre")
    private String unit;
    
    // Price of the product
    private Double price;

    /**
     * Default constructor for creating an empty Product instance.
     */
    public Product() {
    }

    /**
     * Constructs a Product with specified details.
     *
     * @param productNumber The product's unique identifier
     * @param name The name of the product
     * @param unit The unit of measurement for the product
     * @param price The price of the product
     */
    public Product(int productNumber, String name, String unit, Double price) {
        this.productNumber = productNumber;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }

    // Getters and Setters
    
    /**
     * Gets the product number.
     *
     * @return the product number
     */
    public int getProductNumber() {
        return productNumber;
    }

    /**
     * Sets the product number.
     *
     * @param productNumber the new product number
     */
    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
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
     * Sets the name of the product.
     *
     * @param name the new name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unit of measurement for the product.
     *
     * @return the unit of measurement
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of measurement for the product.
     *
     * @param unit the new unit of measurement
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price of the product
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Sets the price of the product.
     *
     * @param price the new price of the product
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Returns a string representation of the Product for logging purposes.
     * <p>
     * Provides a human-readable form of the product entity, including its product number, name, unit, and price.
     *
     * @return a string detailing the product's information
     */
    @Override
    public String toString() {
        return "Product{" +
                "productNumber=" + productNumber +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                '}';
    }
}
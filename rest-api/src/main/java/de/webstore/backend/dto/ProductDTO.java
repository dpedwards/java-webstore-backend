/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Data Transfer Object (DTO) for Product entities.
 * <p>
 * This class is used to transfer product data between processes or through network calls without exposing domain models.
 */
@Schema(description = "Data Transfer Object for Product")
public class ProductDTO {
    
    // Unique product identifier
    @Schema(hidden = true) // This hides the productId property in Swagger UI
    private String productId;
    
    // Name of the product
    @Schema(description = "Name of the product", example = "Ice Cream")
    private String name;
    
    // Measurement unit of the product (e.g., "kg", "liters")
    @Schema(description = "Measurement unit of the product", example = "liters")
    private String unit;
    
    // Price of the product
    @Schema(description = "Price of the product", example = "5.99")
    private BigDecimal price;

    /**
     * Default constructor.
     */
    public ProductDTO() {}

    /**
     * Constructs a ProductDTO with specified details.
     *
     * @param productId Unique identifier for the product
     * @param name Name of the product
     * @param unit Measurement unit of the product
     * @param price Price of the product
     */
    public ProductDTO(String productId, String name, String unit, BigDecimal price) {
        this.productId = productId;
        this.name = name;
        this.unit = unit;
        this.price = price;
    }

    // Getters
    
    /**
     * Gets the product ID.
     *
     * @return the product ID
     */
    public String getProductId() {
        return productId;
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
     * Sets the product ID.
     *
     * @param productId the new product ID
     */
    public void setProductId(String productId) {
        this.productId = productId;
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
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", price=" + price +
                '}';
    }
}
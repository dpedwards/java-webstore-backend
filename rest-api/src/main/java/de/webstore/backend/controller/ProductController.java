/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.webstore.backend.dto.ProductDTO;
import de.webstore.backend.dto.ProductUpdateDTO;
import de.webstore.backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controller for product-related operations.
 * 
 * <p>Supports adding, updating, retrieving, and deleting products.
 */
@RestController
@RequestMapping("/api/de/v1/product")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a ProductController with the given ProductService.
     *
     * @param productService the service to handle product operations
     */
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all products and returns them in a list.
     * 
     * <p>Returns HTTP status 200 along with the list of all product data.</p>
     *
     * @return a ResponseEntity containing a list of all ProductDTOs
     */
    @GetMapping("/all")
    @Operation(summary = "Retrieve all products", responses = {
        @ApiResponse(responseCode = "200", description = "List of all products",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = ProductDTO.class)))
    })
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a product by its ID and returns it.
     * 
     * <p>Returns HTTP status 200 along with the product data if the product is found.
     * Returns HTTP status 404 if the product with the specified ID is not found.</p>
     *
     * @param productId the ID of the product to retrieve
     * @return a ResponseEntity containing the requested ProductDTO or a not found status
     */
    @GetMapping("/{productId}")
    @Operation(summary = "Retrieve a product by its ID", responses = {
        @ApiResponse(responseCode = "200", description = "Product found",
                     content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    public ResponseEntity<?> getProductById(@PathVariable String productId) {
        ProductDTO product = productService.findById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID " + productId + " not found.");
        }
    }

    /**
     * Adds a new product and returns it.
     * 
     * <p>Validates the input data before creating the product.
     * If the product is successfully created, returns HTTP status 201 along with the product.
     * Returns HTTP status 400 for bad requests, such as validation errors.
     * Returns HTTP status 500 for internal errors during the creation process.</p>
     *
     * @param productDTO the product data to create
     * @return a ResponseEntity with the created ProductDTO or an error message
     */
    @PostMapping("/add")
    @Operation(summary = "Add a new product", responses = {
        @ApiResponse(responseCode = "201", description = "Product created successfully",
                     content = @Content(schema = @Schema(implementation = ProductDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request, invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addProduct(@RequestBody ProductDTO productDTO) {
        try {
            // Assuming productService.addProduct() validates the input and throws IllegalArgumentException for validation errors
            ProductDTO createdProduct = productService.addProduct(productDTO);
            // Assume productId is auto-generated and checked for non-zero to confirm creation
            if (createdProduct != null && createdProduct.getProductId() != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
            } else {
                // This case may be redundant if productService.addProduct() handles all validation and exception scenarios
                return ResponseEntity.badRequest().body("Invalid product data provided");
            }
        } catch (IllegalArgumentException e) {
            // Handle validation errors, e.g., missing name, unit, or price
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Catch other exceptions, indicating possible internal errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the product");
        }
    }

    /**
     * Updates the attributes of an existing product.
     * Returns HTTP status code 200 if the update was successful, HTTP status code 500 if there is a database error, 
     * and HTTP status code 404 if everything is okay with the database but the ID was not found.
     *
     * @param productId the ID of the product to update
     * @param productUpdateDTO the updated product data
     * @return a ResponseEntity containing the updated ProductDTO or an appropriate error message
     */
    @PutMapping("/update/{productId}")
    @Operation(summary = "Update a product by its ID", responses = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully", 
                     content = @Content(schema = @Schema(implementation = ProductUpdateDTO.class))),
        @ApiResponse(responseCode = "404", description = "Product ID not found", 
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error", 
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> updateProduct(@PathVariable String productId, @RequestBody ProductUpdateDTO productUpdateDTO) {
        try {
            ProductUpdateDTO updatedProduct = productService.updateProduct(productId, productUpdateDTO);
            if (updatedProduct != null) {
                // Product updated successfully
                return ResponseEntity.ok(updatedProduct);
            } else {
                // Product ID not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID " + productId + " not found.");
            }
        } catch (DataAccessException e) {
            // Database error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred: " + e.getMessage());
        } catch (Exception e) {
            // Other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Deletes a product by its ID.
     * 
     * <p>Returns HTTP status code 200 if the product was successfully deleted.
     * Returns HTTP status code 404 if the product is not found, 
     * and HTTP status code 500 if a database error occurs.</p>
     *
     * @param productId the ID of the product to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "Delete a product by its ID", responses = {
        @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        try {
            boolean isDeleted = productService.deleteProduct(productId);
            if (isDeleted) {
                return ResponseEntity.ok().body("Product deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID " + productId + " not found.");
            }
        } catch (Exception e) {
            // Log the exception details here
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the product.");
        }
    }
}
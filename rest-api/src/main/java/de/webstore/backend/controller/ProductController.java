/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.webstore.backend.dto.ProductDTO;
import de.webstore.backend.dto.ProductUpdateDTO;
import de.webstore.backend.service.ProductService;

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
     * Retrieves all products.
     *
     * @return a ResponseEntity containing a list of all ProductDTOs
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return a ResponseEntity containing the requested ProductDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO product = productService.findById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a new product.
     * 
     * <p>Returns the created product on success. Returns a BadRequest status if the product number is not set or if an error occurs.
     *
     * @param productDTO the product to add
     * @return a ResponseEntity containing the created ProductDTO
     */
    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.addProduct(productDTO);
        if (createdProduct != null && createdProduct.getProductNumber() != 0) {
            return ResponseEntity.ok(createdProduct);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Updates the attributes of an existing product.
     * 
     * <p>Returns the updated product on success. Returns a NotFound status if the product is not found.
     *
     * @param id the ID of the product to update
     * @param productDTO the updated product data
     * @return a ResponseEntity containing the updated ProductDTO
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<ProductUpdateDTO> updateProduct(@PathVariable int id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        ProductUpdateDTO updatedProduct = productService.updateProduct(id, productUpdateDTO);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a product.
     * 
     * <p>Returns an OK status on success.
     *
     * @param id the ID of the product to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}
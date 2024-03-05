/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.webstore.backend.dto.WarehouseDTO;
import de.webstore.backend.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Controller for warehouse-related operations.
 * 
 * <p>Supports adding and reducing product quantities in the warehouse, as well as retrieving total product quantities and warehouse information.
 */
@RestController
@RequestMapping("/api/de/v1/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    /**
     * Constructs a WarehouseController with the specified WarehouseService.
     *
     * @param warehouseService the service to handle warehouse operations
     */
    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Retrieves all warehouse entries.
     *
     * @return a ResponseEntity containing a list of all WarehouseDTOs
     */
    @GetMapping("/all")
    @Operation(summary = "Get all warehouse entries", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of all warehouse entries",
                    content = {@Content(mediaType = "application/json", 
                            schema = @Schema(implementation = WarehouseDTO.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouseEntries() {
        List<WarehouseDTO> warehouses = warehouseService.findAll();
        return ResponseEntity.ok(warehouses);
    }

    /**
     * Retrieves a specific warehouse by its ID.
     * 
     * <p>This endpoint retrieves warehouse details based on the provided warehouse ID.
     * It returns the warehouse details if found, or a 404 Not Found status if the warehouse does not exist.
     * 
     * @param warehouseId the ID of the warehouse to retrieve
     * @return a ResponseEntity containing the requested WarehouseDTO or a 404 status if not found
     */
    @GetMapping("/{warehouseId}")
    @Operation(summary = "Get a warehouse by its ID", description = "Retrieves warehouse details for the given warehouse ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the warehouse",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WarehouseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Warehouse not found for the provided ID",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                    content = @Content)
    })
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable int warehouseId) {
        WarehouseDTO warehouse = warehouseService.findById(warehouseId);
        if (warehouse != null) {
            return ResponseEntity.ok(warehouse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a specified quantity of a product to a warehouse.
     * 
     * <p>This operation updates the stock quantity for a specified product in a specified warehouse by adding the given quantity. 
     * If successful, a confirmation is returned.
     * 
     * @param productId the ID of the product to add quantity to
     * @param warehouseId the ID of the warehouse where quantity is to be added
     * @param quantity the quantity to add
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/add/product/{productId}/warehouse/{warehouseId}")
    @Operation(summary = "Add product quantity to a warehouse", description = "Adds a specified quantity of a product to the specified warehouse.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully added the product quantity to the warehouse",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid inputs",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product or warehouse not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                    content = @Content)
    })
    public ResponseEntity<?> addProductQuantity(@PathVariable int productId, @PathVariable int warehouseId, @org.springframework.web.bind.annotation.RequestBody int quantity) {
        try {
            warehouseService.addProductQuantity(productId, warehouseId, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to add product quantity: " + e.getMessage());
        }
    }

    /**
     * Reduces a specified quantity of a product in a warehouse.
     * 
     * <p>This operation decreases the stock quantity for a specified product in a specified warehouse by the given quantity. 
     * If the operation is successful, a confirmation is returned.
     *
     * @param productId the ID of the product to reduce the quantity of
     * @param warehouseId the ID of the warehouse where the quantity is to be reduced
     * @param quantity the quantity to reduce
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/reduce/product/{productId}/warehouse/{warehouseId}")
    @Operation(summary = "Reduce product quantity in a warehouse", description = "Reduces a specified quantity of a product in the specified warehouse.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully reduced the product quantity in the warehouse",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid inputs",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Product or warehouse not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                    content = @Content)
    })
    public ResponseEntity<?> reduceProductQuantity(@PathVariable int productId, @PathVariable int warehouseId, @RequestBody int quantity) {
        try {
            warehouseService.reduceProductQuantity(productId, warehouseId, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to reduce product quantity: " + e.getMessage());
        }
    }

    /**
     * Retrieves the total quantity of a specific product across all warehouses.
     * 
     * <p>This operation sums up the quantity of the specified product available across all warehouses and returns the total amount.
     *
     * @param productId the ID of the product to calculate the total quantity for
     * @return a ResponseEntity containing the total quantity
     */
    @GetMapping("/product/{productId}/total")
    @Operation(summary = "Get total product quantity across all warehouses", description = "Calculates and returns the total quantity of a specified product across all warehouses.", responses = {
            @ApiResponse(responseCode = "200", description = "Total quantity of the product across all warehouses",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                    content = @Content)
    })
    public ResponseEntity<Integer> getTotalProductQuantity(@PathVariable int productId) {
        try {
            int totalQuantity = warehouseService.calculateTotalProductQuantity(productId);
            return ResponseEntity.ok(totalQuantity);
        } catch (Exception e) {
            // Assuming a custom exception for a non-existent product is defined
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Or use a more appropriate exception and error message
        }
    }
}
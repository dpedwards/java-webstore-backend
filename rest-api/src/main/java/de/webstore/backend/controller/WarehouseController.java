/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.webstore.backend.dto.WarehouseDTO;
import de.webstore.backend.exception.ErrorResponse;
import de.webstore.backend.exception.ProductNotFoundException;
import de.webstore.backend.exception.WarehouseNotFoundException;
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
        List<WarehouseDTO> warehouses = warehouseService.findAllActive();
        return ResponseEntity.ok(warehouses);
    }

    /**
     * Retrieves a specific warehouse by its Number.
     * 
     * <p>This endpoint retrieves warehouse details based on the provided warehouse Number.
     * It returns the warehouse details if found, or a 404 Not Found status if the warehouse does not exist.
     * 
     * @param warehouseNumber the Number of the warehouse to retrieve
     * @return a ResponseEntity containing the requested WarehouseDTO or a 404 status if not found
     */
    @GetMapping("/{warehouseNumber}")
    @Operation(summary = "Get a warehouse by its Number", description = "Retrieves warehouse details for the given warehouse Number.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the warehouse",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WarehouseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Warehouse not found for the provided Number",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                    content = @Content)
    })
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable int warehouseNumber) {
        WarehouseDTO warehouse = warehouseService.findById(warehouseNumber);
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
     * @param productId the Number of the product to add quantity to
     * @param warehouseNumber the Number of the warehouse where quantity is to be added
     * @param quantity the quantity to add
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/add/product/{productId}/warehouse/{warehouseNumber}")
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
    public ResponseEntity<?> addProductQuantity(@PathVariable String productId, @PathVariable int warehouseNumber, @RequestBody int quantity) {
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive.");
            }
            warehouseService.addProductQuantityAndUpdateWarehouse(productId, warehouseNumber, quantity);
            return ResponseEntity.ok().build();
        } catch (WarehouseNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
            } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to add product quantity."));
        }
    }

    /**
     * Reduces a specified quantity of a product in a warehouse.
     * 
     * <p>This operation decreases the stock quantity for a specified product in a specified warehouse by the given quantity. 
     * If the operation is successful, a confirmation is returned.
     *
     * @param productId the Number of the product to reduce the quantity of
     * @param warehouseNumber the Number of the warehouse where the quantity is to be reduced
     * @param quantity the quantity to reduce
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/reduce/product/{productId}/warehouse/{warehouseNumber}")
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
    public ResponseEntity<?> reduceProductQuantity(@PathVariable String productId, @PathVariable int warehouseNumber, @RequestBody int quantity) {
        try {
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be positive.");
            }
            warehouseService.reduceProductQuantityAndUpdateWarehouse(productId, warehouseNumber, quantity);
            return ResponseEntity.ok().build();
        } catch (WarehouseNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Failed to reduce product quantity."));
        }
    }
    
    /**
     * Retrieves the total quantity of a specific product across all warehouses.
     * 
     * <p>This operation sums up the quantity of the specified product available across all warehouses and returns the total amount.
     *
     * @param productId the Number of the product to calculate the total quantity for
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
    public ResponseEntity<?> getTotalProductQuantity(@PathVariable String productId) {
        try {
                int totalQuantity = warehouseService.calculateTotalProductQuantity(productId);
                return ResponseEntity.ok().body(totalQuantity);
        } catch (ProductNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred while getting total product quantity across all warehouses."));
        }
    }
}
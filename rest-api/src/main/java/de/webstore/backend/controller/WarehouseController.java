/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.webstore.backend.dto.WarehouseDTO;
import de.webstore.backend.service.WarehouseService;

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
    @GetMapping("/")
    public ResponseEntity<List<WarehouseDTO>> getAllWarehouseEntries() {
        List<WarehouseDTO> warehouses = warehouseService.findAll();
        return ResponseEntity.ok(warehouses);
    }

    /**
     * Retrieves a specific warehouse by its ID.
     *
     * @param id the ID of the warehouse to retrieve
     * @return a ResponseEntity containing the requested WarehouseDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(@PathVariable int id) {
        WarehouseDTO warehouse = warehouseService.findById(id);
        if (warehouse != null) {
            return ResponseEntity.ok(warehouse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a specified quantity of a product to a warehouse.
     * 
     * <p>After successfully adding, returns a confirmation.
     *
     * @param productId the ID of the product to add quantity to
     * @param warehouseId the ID of the warehouse where quantity is to be added
     * @param quantity the quantity to add
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/product/{productId}/warehouse/{warehouseId}/add")
    public ResponseEntity<?> addProductQuantity(@PathVariable int productId, @PathVariable int warehouseId, @RequestBody int quantity) {
        warehouseService.addProductQuantity(productId, warehouseId, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * Reduces a specified quantity of a product in a warehouse.
     * 
     * <p>Similar to addProductQuantity, but reduces the product quantity instead.
     *
     * @param productId the ID of the product to reduce quantity of
     * @param warehouseId the ID of the warehouse where quantity is to be reduced
     * @param quantity the quantity to reduce
     * @return a ResponseEntity indicating the result of the operation
     */
    @PostMapping("/product/{productId}/warehouse/{warehouseId}/reduce")
    public ResponseEntity<?> reduceProductQuantity(@PathVariable int productId, @PathVariable int warehouseId, @RequestBody int quantity) {
        warehouseService.reduceProductQuantity(productId, warehouseId, quantity);
        return ResponseEntity.ok().build();
    }

    /**
     * Retrieves the total quantity of a specific product across all warehouses.
     *
     * @param productId the ID of the product to calculate total quantity for
     * @return a ResponseEntity containing the total quantity
     */
    @GetMapping("/product/{productId}/total")
    public ResponseEntity<Integer> getTotalProductQuantity(@PathVariable int productId) {
        int totalQuantity = warehouseService.calculateTotalProductQuantity(productId);
        return ResponseEntity.ok(totalQuantity);
    }
}
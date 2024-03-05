/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import de.webstore.backend.dto.OrderDTO;
import de.webstore.backend.dto.PositionDTO;
import de.webstore.backend.service.OrderService;

/**
 * REST controller for managing orders.
 */
@RestController
@RequestMapping("/api/de/v1/order")
public class OrderController {

    private final OrderService orderService;

    /**
     * Constructs an OrderController with the specified OrderService.
     *
     * @param orderService the order service to use for order operations
     */
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Retrieves all orders.
     *
     * @return a ResponseEntity containing a list of OrderDTOs
     */
    @GetMapping("/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves a specific order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing the requested OrderDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable int id) {
        return ResponseEntity.ok(orderService.findById(id));
    }

    /**
     * Creates a new order.
     *
     * @param orderDTO the order to create
     * @return a ResponseEntity containing the created OrderDTO
     */
    @PostMapping("/add")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        if (createdOrder != null && createdOrder.getOrderNumber() != 0) {
            return ResponseEntity.ok(createdOrder);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Adds a position to an existing order.
     *
     * @param orderId    the ID of the order to add the position to
     * @param positionDTO the position to add to the order
     * @return a ResponseEntity containing the created PositionDTO
     */
    @PostMapping("/add/{orderId}/position")
    public ResponseEntity<PositionDTO> addOrderPosition(@PathVariable int orderId, @RequestBody PositionDTO positionDTO) {
        positionDTO.setOrderNumber(orderId); // Sets the order ID to the position
        PositionDTO createdPosition = orderService.addOrderPosition(positionDTO);
        if (createdPosition != null && createdPosition.getPositionNumber() != 0) {
            return ResponseEntity.ok(createdPosition);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Deletes a position from an order.
     *
     * @param orderId    the ID of the order from which to delete the position
     * @param positionId the ID of the position to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/delete/{orderId}/position/{positionId}")
    public ResponseEntity<?> deleteOrderPosition(@PathVariable int orderId, @PathVariable int positionId) {
        orderService.deleteOrderPosition(positionId);
        return ResponseEntity.ok().build();
    }

    /**
     * Deletes an order by its ID.
     *
     * @param orderId the ID of the order to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable int orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }

    /**
     * Closes an order by its ID.
     *
     * @param orderId the ID of the order to close
     * @return a ResponseEntity indicating the result of the operation
     */
    @PutMapping("/close/{orderId}")
    public ResponseEntity<?> closeOrder(@PathVariable int orderId) {
        boolean isClosed = orderService.closeOrder(orderId);
        if (isClosed) {
            return ResponseEntity.ok().build();
        } else {
            // Could not close the order due to various reasons (like insufficient stock)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order could not be closed due to insufficient stock or other issues.");
        }
    }
}
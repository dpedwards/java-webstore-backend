/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import de.webstore.backend.dto.OrderDTO;
import de.webstore.backend.dto.PositionDTO;
import de.webstore.backend.exception.ErrorResponse;
import de.webstore.backend.exception.InsufficientStockException;
import de.webstore.backend.exception.OrderClosedException;
import de.webstore.backend.exception.OrderNotFoundException;
import de.webstore.backend.exception.PositionNotFoundException;
import de.webstore.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
     * Retrieves all orders and returns them in a list.
     * 
     * <p>Returns HTTP status 200 along with the list of all order data.</p>
     *
     * @return a ResponseEntity containing a list of all OrderDTOs
     */
    @GetMapping("/all")
    @Operation(summary = "Retrieve all orders", responses = {
        @ApiResponse(responseCode = "200", description = "List of all orders",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = OrderDTO.class)))
    })
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves a specific order by its ID.
     * 
     * <p>Returns HTTP status 200 along with the order data if found, otherwise returns HTTP status 404.</p>
     *
     * @param id the ID of the order to retrieve
     * @return a ResponseEntity containing the requested OrderDTO or a not found response
     */
    @GetMapping("/{orderId}")
    @Operation(summary = "Retrieve a specific order by ID", responses = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content)
    })
    public ResponseEntity<?> getOrderById(@PathVariable String orderId) {
        OrderDTO order = orderService.findById(orderId);
        if (order != null) {
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Creates a new order and returns the created order with its assigned order ID.
     * If the creation process encounters any issue (e.g., validation errors), it responds with an appropriate HTTP status code.
     *
     * @param orderDTO the order data to create
     * @return a ResponseEntity containing the created OrderDTO on success, or an appropriate error response
     */
    @PostMapping("/add")
    @Operation(summary = "Create a new order", responses = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, validation errors or missing information",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, could not process the request",
                    content = @Content)
    })
    public ResponseEntity<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO createdOrder = orderService.createOrder(orderDTO);
            // Assuming createOrder throws an exception if order creation fails or returns null.
            if (createdOrder != null) {
                return ResponseEntity.ok(createdOrder);
            } else {
                // In case the order creation process fails but does not throw an exception.
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Could not create the order due to an unexpected error."));
            }
        } catch (Exception e) {
            // Handling any exception that might occur during the order creation process.
            return ResponseEntity.badRequest().body(new OrderDTO()); // Adjust based on the error handling policy
        }
    }

  /**
     * Adds a new position to an existing order and returns the added position.
     * Responds with an appropriate HTTP status code based on the outcome of the operation.
     *
     * @param orderId the ID of the order to add the position to
     * @param positionDTO the position data to add
     * @return a ResponseEntity containing the added PositionDTO on success, or an appropriate error response
     */
    @PostMapping("/{orderId}/add/new/position")
    @Operation(summary = "Add a new position with a product and a quantity to an existing open order", responses = {
            @ApiResponse(responseCode = "200", description = "Position added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PositionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request, validation errors or missing information",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Order is closed",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error, could not process the request",
                    content = @Content)
    })
    public ResponseEntity<?> addOrderPosition(@PathVariable String orderId, @RequestBody PositionDTO positionDTO) {
        try {
            // Verify if the order exists and is open
            if(!orderService.checkOrderExistsAndOpen(orderId)) {
                throw new OrderClosedException("Order with ID " + orderId + " is closed or does not exist.");
            }
    
            PositionDTO createdPosition = orderService.addOrderPosition(orderId, positionDTO);
            return ResponseEntity.ok(createdPosition);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Order with ID " + orderId + " not found."));
        } catch (OrderClosedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Order with ID " + orderId + " is closed."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Could not process the request."));
        }
    }

    /**
     * Deletes a position from an order.
     * 
     * Verifies if the position belongs to the given order and deletes it if true.
     * If the position or order does not exist, appropriate exceptions are thrown.
     *
     * @param orderId    the ID of the order from which to delete the position
     * @param positionId the ID of the position to delete
     * @return a ResponseEntity indicating the result of the operation
     */
    @DeleteMapping("/order/{orderId}/position/{positionId}")
    @Operation(summary = "Delete a position from an order",
               description = "Deletes a specific position from an order if it exists and belongs to the specified order. It ensures the order is not closed.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Position deleted successfully"),
                   @ApiResponse(responseCode = "404", description = "Position not found or does not belong to the specified order"),
                   @ApiResponse(responseCode = "409", description = "Order is closed and cannot be modified")
               })
    public ResponseEntity<?> deleteOrderPosition(@PathVariable String orderId, @PathVariable String positionId) {
        try {
            orderService.deleteOrderPosition(orderId, positionId);
            return ResponseEntity.ok().build();
        } catch (OrderClosedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(e.getMessage()));
        } catch (PositionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An unexpected error occurred."));
        }
    }

    /**
     * Deletes an order identified by its unique ID. Validates that the order is not already closed before attempting deletion.
     * If the order is closed or does not exist, appropriate error responses are returned.
     *
     * @param orderId The unique identifier of the order to delete.
     * @return ResponseEntity indicating the result of the operation with appropriate HTTP status code and body.
     *         Returns OK (200) if the order was successfully deleted.
     *         Returns NOT FOUND (404) if the order does not exist.
     *         Returns CONFLICT (409) if the order is closed and cannot be deleted.
     *         Returns INTERNAL SERVER ERROR (500) for any other errors during the process.
     */
    @DeleteMapping("/order/delete/{orderId}")
    @Operation(summary = "Delete an order",
               description = "Deletes an order by its ID. Validates if the order is not closed before deletion. If the order does not exist or is already closed, appropriate error responses are returned.",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Order successfully deleted"),
                   @ApiResponse(responseCode = "404", description = "Order not found"),
                   @ApiResponse(responseCode = "409", description = "Order is closed and cannot be deleted"),
                   @ApiResponse(responseCode = "500", description = "Internal server error")
               })
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (OrderNotFoundException e) {
            // Handling case where the specified order ID does not exist in the database.
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Order with ID " + orderId + " not found."));
        } catch (OrderClosedException e) {
            // Handling case where the specified order is already closed and cannot be deleted.
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Order with ID " + orderId + " is closed and cannot be deleted."));
        } catch (Exception e) {
            // Handling unexpected exceptions during the deletion process.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred while deleting the order."));
        }
    }

    /**
     * Closes an order by its ID, ensuring all preconditions for closing the order are met.
     * 
     * Responds with OK status if the order is successfully closed. If preconditions, such as sufficient stock, are not met, or if the order does not exist,
     * appropriate error responses are returned.
     *
     * @param orderId the ID of the order to close
     * @return a ResponseEntity indicating the result of the operation
     */
    @PutMapping("/close/{orderId}")
    @Operation(summary = "Close an order",
            description = "Closes an order by its ID after verifying all preconditions, such as sufficient stock levels, are met. If the order cannot be closed due to unmet preconditions or if the order does not exist, appropriate error responses are indicated.",
            responses = {
                @ApiResponse(responseCode = "200", description = "Order successfully closed"),
                @ApiResponse(responseCode = "404", description = "Order not found"),
                @ApiResponse(responseCode = "409", description = "Preconditions for closing the order are not met (e.g., insufficient stock)"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<?> closeOrder(@PathVariable String orderId) {
        try {
            // Attempt to close the order
            orderService.closeOrder(orderId);
            // If successful, return 200 OK
            return ResponseEntity.ok().build();
        } catch (OrderNotFoundException e) {
            // If the order is not found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Order with ID " + orderId + " not found."));
        } catch (InsufficientStockException e) {
            // If preconditions are not met, return 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Order could not be closed due to insufficient stock."));
        } catch (Exception e) {
            // For any other errors, return 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("An error occurred while closing the order."));
        }
    }    
}
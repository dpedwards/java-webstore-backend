/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

/**
 * A custom exception that signals insufficient stock available to fulfill a request.
 * <p>
 * This exception is used in scenarios where available stock is not sufficient
 * to cover the quantity of a product in an order or request. Using this
 * exception helps to explicitly handle such situations and take appropriate actions.
 */
public class InsufficientStockException extends RuntimeException {

    /**
     * Creates a new instance of {@code InsufficientStockException} without a detail message.
     */
    public InsufficientStockException() {
        super();
    }

    /**
     * Constructs an {@code InsufficientStockException} with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for later retrieval by
     *                the {@link #getMessage()} method.
     */
    public InsufficientStockException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code InsufficientStockException} with the specified detail message and cause.
     *
     * @param message the detail message (which is later retrieved by the {@link #getMessage()} method).
     * @param cause   the cause (which is later retrieved by the {@link #getCause()} method). (A {@code null} value
     *                is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InsufficientStockException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an {@code InsufficientStockException} with the specified cause and a detail message
     * of {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of
     * {@code cause}). This constructor is useful for exceptions that are little more than wrappers for other throwables.
     *
     * @param cause the cause (which is later retrieved by the {@link #getCause()} method). (A {@code null} value
     *              is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public InsufficientStockException(Throwable cause) {
        super(cause);
    }
}
/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

/**
 * Exception class for handling cases where a product cannot be deleted because it is part of an order.
 * <p>
 * This exception is thrown when an attempt is made to delete a product that is currently referenced in one or more
 * order positions. Extending {@link RuntimeException}, it simplifies error handling by not requiring
 * explicit catching or declaration in method signatures, thus maintaining cleaner code architecture where
 * checked exceptions are not preferred.
 */
public class ProductInOrderException extends RuntimeException {

    /**
     * Constructs a new {@code ProductInOrderException} with the specified detail message.
     * <p>
     * The detail message provides more information about the reason why the product cannot be deleted,
     * usually because it is part of an existing order. This message is stored and later accessible
     * through the {@link #getMessage()} method.
     *
     * @param message the detail message explaining why the product cannot be deleted. The detail message
     *                is stored for later retrieval by the {@link #getMessage()} method.
     */
    public ProductInOrderException(String message) {
        super(message);
    }
}
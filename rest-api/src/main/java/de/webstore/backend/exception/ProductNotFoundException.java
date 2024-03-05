/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

/**
 * Exception class for handling cases where a product cannot be found.
 * <p>
 * This exception is thrown when an operation involving a product is attempted but
 * the product does not exist in the database or the context where it is expected.
 * Extending {@link RuntimeException}, it does not require explicit catching or
 * declaration in method signatures, allowing for cleaner code where checked
 * exceptions are not necessary or desired.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Constructs a new {@code ProductNotFoundException} with the specified detail message.
     * <p>
     * The detail message is saved for later retrieval by the {@link #getMessage()} method.
     *
     * @param message the detail message. The detail message is saved for later retrieval
     *                by the {@link #getMessage()} method.
     */
    public ProductNotFoundException(String message) {
        super(message);
    }
}
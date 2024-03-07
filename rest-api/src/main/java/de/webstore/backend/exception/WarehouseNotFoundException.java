/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

/**
 * This exception is thrown to indicate that either a product or a warehouse could not be found.
 * <p>
 * It extends {@link RuntimeException}, meaning it is an unchecked exception that does not need
 * to be declared in a method's or constructor's {@code throws} clause if they can be thrown
 * during the execution of the method or constructor and propagated up the call stack.
 */
public class WarehouseNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new exception with the specified detail message. The cause is not initialized,
     * and may subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the
     *                {@link #getMessage()} method.
     */
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
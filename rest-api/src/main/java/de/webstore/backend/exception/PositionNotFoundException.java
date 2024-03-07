/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

/**
 * Custom exception to be thrown when an position is not found.
 */
public class PositionNotFoundException extends RuntimeException {

    /**
     * Constructs a new PositionNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     */
    public PositionNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new PositionNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   the cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public PositionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new PositionNotFoundException with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically contains the class and detail message of cause).
     *
     * @param cause the cause (which is saved for later retrieval by the getCause() method). (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public PositionNotFoundException(Throwable cause) {
        super(cause);
    }
}
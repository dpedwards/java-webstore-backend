/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

public class OrderClosedException extends Exception {
    
    // Constructor that accepts a message
    public OrderClosedException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public OrderClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
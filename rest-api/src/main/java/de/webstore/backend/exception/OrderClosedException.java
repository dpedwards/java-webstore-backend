/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

public class OrderClosedException extends RuntimeException {
    
    // Constructor that accepts a message
    public OrderClosedException(String message) {
        super(message);
    }
}
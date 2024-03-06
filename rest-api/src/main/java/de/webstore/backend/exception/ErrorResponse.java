/* Copyright Davain Pablo Edwards core8@gmx.net. Licensed https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en */
package de.webstore.backend.exception;

/**
 * Represents an error response for API calls.
 * This class encapsulates error message details that can be returned to the client.
 */
public class ErrorResponse {

    /**
     * The error message describing the nature of the error.
     */
    private String error;

    /**
     * Constructs a new ErrorResponse with a specific error message.
     *
     * @param error the error message to be encapsulated in this error response.
     */
    public ErrorResponse(String error) {
        this.error = error;
    }

    /**
     * Retrieves the error message contained in this error response.
     *
     * @return the error message.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for this error response.
     *
     * @param error the new error message.
     */
    public void setError(String error) {
        this.error = error;
    }
}
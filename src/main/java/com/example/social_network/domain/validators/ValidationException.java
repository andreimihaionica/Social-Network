package com.example.social_network.domain.validators;

public class ValidationException extends RuntimeException {

    /**
     * Implicit constructor
     */
    public ValidationException() {}

    /**
     * Constructor with parameters
     * @param message - String
     */
    public ValidationException(String message) {
        super(message);
    }

    /**
     * Constructor with parameters
     * @param message - String
     * @param cause - Throwable
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with parameters
     * @param cause - Throwable
     */
    public ValidationException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor with parameters
     * @param message - String
     * @param cause - Throwable
     * @param enableSuppression - boolean
     * @param writableStackTrace - boolean
     */
    public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

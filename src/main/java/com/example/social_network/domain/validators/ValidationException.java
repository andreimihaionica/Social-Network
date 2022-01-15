package com.example.social_network.domain.validators;

public class ValidationException extends RuntimeException {
    /**
     * Constructor with parameters
     *
     * @param message - String
     */
    public ValidationException(String message) {
        super(message);
    }
}

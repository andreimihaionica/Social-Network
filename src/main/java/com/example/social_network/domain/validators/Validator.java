package com.example.social_network.domain.validators;

/**
 * Represent the validator interface for object T
 * @param <T> -
 */
public interface Validator<T> {
    /**
     * Validate entity
     * @param entity - Entity
     * @throws ValidationException -
     */
    void validate(T entity) throws ValidationException;
}

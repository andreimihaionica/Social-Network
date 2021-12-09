package com.example.social_network.domain.validators;

import com.example.social_network.domain.User;

public class UserValidator implements Validator<User> {
    /**
     * Validate User
     * @param entity - User
     * @throws ValidationException -
     */
    @Override
    public void validate(User entity) throws ValidationException {
        String username = entity.getUsername().trim();
        if (username == null || username.isEmpty()) {
            throw new ValidationException("Invalid username");
        }
    }
}

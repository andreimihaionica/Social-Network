package com.example.social_network.domain.validators;

import com.example.social_network.domain.Password;

public class PasswordValidator implements Validator<Password> {
    @Override
    public void validate(Password entity) throws ValidationException {
        if (entity.getPassword().length() < 6 || entity.getPassword().contains(" ")) {
            throw new ValidationException("Invalid password!");
        }
    }
}

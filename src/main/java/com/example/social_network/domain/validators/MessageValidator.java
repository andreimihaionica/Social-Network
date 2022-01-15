package com.example.social_network.domain.validators;

import com.example.social_network.domain.Message;

public class MessageValidator implements Validator<Message> {
    /**
     * Validate message
     *
     * @param entity - Message
     * @throws ValidationException -
     */
    @Override
    public void validate(Message entity) throws ValidationException {
        String message = entity.getMessage().trim();
        if (message.isEmpty())
            throw new ValidationException("Invalid message!");
    }
}

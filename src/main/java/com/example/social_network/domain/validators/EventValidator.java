package com.example.social_network.domain.validators;

import com.example.social_network.domain.Event;

public class EventValidator implements Validator<Event> {
    @Override
    public void validate(Event entity) throws ValidationException {
        if(entity.getName().isEmpty())
            throw new ValidationException("Invalid event name!");
        if(entity.getLocation().isEmpty())
            throw new ValidationException("Invalid event location!");
    }
}

package com.example.social_network.domain.validators;

import com.example.social_network.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    /**
     * Validate friendship
     * @param entity - Friendship
     * @throws ValidationException -
     */
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String date = entity.getDate().trim();
        if (date == null || date.isEmpty() || !(date.matches("^[0-3][0-9]/[0-1][0-9]/[1-2][0-9][0-9][0-9]$"))) {
            throw new ValidationException("Invalid friendship date");
        }
    }
}

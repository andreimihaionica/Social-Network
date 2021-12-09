package com.example.social_network.domain;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity Class
 * @param <ID> -
 */
public class Entity <ID> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7331115341259248461L;
    /**
     * ID of entity
     */
    private ID id;

    /**
     * Get the ID for an entity
     * @return ID of entity
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the ID for an entity
     * @param id - new id
     */
    public void setId(ID id) {
        this.id = id;
    }
}

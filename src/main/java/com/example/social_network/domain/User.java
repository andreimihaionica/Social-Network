package com.example.social_network.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User class
 */
public class User extends Entity<Long> {
    /**
     * Username of User
     */
    private String username;

    /**
     * Friend list of User
     */
    private final List<User> friends;

    /**
     * Constructor with parameters
     *
     * @param username - username of User
     */
    public User(String username) {
        this.username = username;
        this.friends = new ArrayList<>();
    }

    /**
     * Sets the username for User
     *
     * @param username - new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the username for User
     *
     * @return username of User
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get the friends for User
     *
     * @return list of User
     */
    public List<User> getFriends() {
        return friends;
    }

    /**
     * Override toString() for User
     *
     * @return String
     */
    @Override
    public String toString() {
        return username;
    }

    /**
     * Override equals(Object o) for User
     *
     * @param o - object to be compared
     * @return true - if Object o equals to User | false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        return getUsername().equals(that.getUsername());
    }

    /**
     * Override hashCode() for User
     *
     * @return hash code for User
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getFriends());
    }
}

package com.example.social_network.domain;

import java.util.Objects;

/**
 * Tuple class
 * @param <E1> - entity
 * @param <E2> - entity
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     * Constructor with parameters
     * @param e1 entity 1
     * @param e2 entity 2
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Get left entity for a Tuple
     * @return entity 1
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * Sets left entity for a Tuple
     * @param e1 - new entity
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * Get right entity for a Tuple
     * @return entity 2
     */
    public E2 getRight() {
        return e2;
    }

    /**
     * Sets right entity for a Tuple
     * @param e2 - new entity
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    /**
     * Override toString() for Tuple
     * @return String
     */
    @Override
    public String toString() {
        return "" + e1 + "," + e2;
    }

    /**
     * Override equals(Object obj) for Tuple
     * @param obj - object to be compared
     * @return true - if Object o equals to User | false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        return this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2);
    }

    /**
     * Override hashCode() for Tuple
     * @return hash code for Tuple
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }
}

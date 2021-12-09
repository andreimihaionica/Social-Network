package com.example.social_network.domain;

public class Friendship extends Entity<Tuple<Long, Long>> {
    /**
     * Friendship date
     */
    String date;
    FriendshipStatus status;

    /**
     * Constructor with parameters
     * @param date - String
     */
    public Friendship(String date, FriendshipStatus status) {
        this.date = date;
        this.status = status;
    }

    /**
     * Get date for Friendship
     * @return date - String
     */
    public String getDate() {
        return date;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    /**
     * Override toString() for Friendship
     * @return String
     */
    @Override
    public String toString() {
        return "Friendship{" +
                getId() +
                " date=" + date +
                " status=" + status.toString() +
                '}';
    }
}

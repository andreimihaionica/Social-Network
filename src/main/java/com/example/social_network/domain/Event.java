package com.example.social_network.domain;

import java.time.LocalDateTime;
import java.util.List;

public class Event extends Entity<Long> {
    private User createdBy;
    private String name;
    private String description;
    private String location;
    private String type;
    private LocalDateTime date;
    private List<User> subscribers;

    public Event(User createdBy, String name, String description, String location, String type, LocalDateTime date, List<User> subscribers) {
        this.createdBy = createdBy;
        this.name = name;
        this.description = description;
        this.location = location;
        this.type = type;
        this.date = date;
        this.subscribers = subscribers;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<User> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<User> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public String toString() {
        return "Event{" +
                "createdBy=" + createdBy +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", subscribers=" + subscribers +
                '}';
    }
}

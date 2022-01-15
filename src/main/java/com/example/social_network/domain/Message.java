package com.example.social_network.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Message extends Entity<Long> {
    private User from;
    private List<User> to;
    private LocalDateTime date;
    private String message;
    private final Long reply;

    public Message(User from, List<User> to, LocalDateTime date, String message, Long reply) {
        this.from = from;
        this.to = to;
        this.date = date;
        this.message = message;
        this.reply = reply;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReply() {
        return reply;
    }

    @Override
    public String toString() {
        return '\'' + message + '\'' +
                " at " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss"));
    }
}

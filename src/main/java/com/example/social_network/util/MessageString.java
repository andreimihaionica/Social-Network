package com.example.social_network.util;

public class MessageString {
    public String from;
    public String to;
    public String message;
    public String date;

    public MessageString(String from, String to, String message, String date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}

package com.example.social_network.util;

import com.example.social_network.domain.Message;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class Conversation {
    public String users;
    public String stringConversation;
    public List<Message> conversation;

    public Conversation(List<Message> conversation) {
        this.conversation = conversation;
        users = "";
        for (var message : conversation) {
            if (!users.contains(message.getFrom().getUsername())) {
                users = users + message.getFrom().getUsername() + ", ";
            }
            for (var user : message.getTo()) {
                if (!users.contains(user.getUsername())) {
                    users = users + user.getUsername() + ", ";
                }
            }
        }
        if (users.length() >= 2) {
            users = users.substring(0, users.length() - 2);
        }
        Message lastMessage = conversation.get(conversation.size() - 1);
        stringConversation = lastMessage.getFrom().getUsername() + " wrote " + '"' + lastMessage.getMessage() + '"' + " at " + lastMessage.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm"));
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getStringConversation() {
        return stringConversation;
    }

    public void setStringConversation(String stringConversation) {
        this.stringConversation = stringConversation;
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public void setConversation(List<Message> conversation) {
        this.conversation = conversation;
    }
}

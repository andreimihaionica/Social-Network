package com.example.social_network.util;

import com.example.social_network.domain.Message;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Conversation {
    public String users;
    public String stringConversation;
    public List<Message> conversation;
    public String date;

    public Conversation(List<Message> conversation, String currentUser) {
        this.conversation = conversation;
        users = "";
        List<String> usernames = new ArrayList<>();
        for (var message : conversation) {
            if (!usernames.contains(message.getFrom().getUsername()) && !Objects.equals(message.getFrom().getUsername(), currentUser)) {
                users = users + message.getFrom().getUsername() + ", ";
                usernames.add(message.getFrom().getUsername());
            }
            for (var user : message.getTo()) {
                if (!usernames.contains(user.getUsername()) && !Objects.equals(user.getUsername(), currentUser)) {
                    users = users + user.getUsername() + ", ";
                    usernames.add(user.getUsername());
                }
            }
        }
        if (users.length() >= 2) {
            users = users.substring(0, users.length() - 2);
        }

        if(users.contains(","))
            users = users + ", " + currentUser;

        Message lastMessage = conversation.get(conversation.size() - 1);

         stringConversation = lastMessage.getFrom().getUsername() + " wrote " + '"' + lastMessage.getMessage() + '"';
         date = lastMessage.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm"));
    }

    public String getUsers() {
        return users;
    }

    public String getStringConversation() {
        return stringConversation;
    }

    public List<Message> getConversation() {
        return conversation;
    }

    public String getDate() {return date;}
    @Override
    public String toString() {
        return "Conversation{" +
                "users='" + users + '\'' +
                ", stringConversation='" + stringConversation + '\'' +
                ", conversation=" + conversation +
                '}';
    }
}

package com.example.social_network.controllers;

import com.example.social_network.domain.Message;
import com.example.social_network.domain.User;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AllConversationsController {
    @FXML
    VBox conversationItems;

    @FXML
    public void initialize() {
        conversationItems.getChildren().clear();

        Node node;
        ConversationItemController controller;

        Service service = SignInController.service;
        Iterable<Message> messageIterable = service.getAllMessages();
        List<Message> messages = new ArrayList<>();
        for (Message message : messageIterable) {
            messages.add(message);
        }
        messages.sort(Comparator.comparing(Message::getDate));

        for (Message message : messages) {
            try {
                if (((Objects.equals(message.getFrom().getUsername(), SignInController.currentUser)) ||
                        (message.getTo().contains(service.getUser(SignInController.currentUser))))
                        && message.getReply() == 0L) {
                    List<Message> messageList = new ArrayList<>();

                    Message message1 = message;
                    messageList.add(message1);

                    while (getReply(Objects.requireNonNull(message1).getId(), messageIterable, service) != null) {
                        message1 = getReply(message1.getId(), messageIterable, service);
                        messageList.add(message1);
                    }

                    Conversation conversation = new Conversation(messageList, SignInController.currentUser);

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/conversationItem.fxml"));
                    node = fxmlLoader.load();

                    controller = fxmlLoader.getController();
                    controller.initialize(conversation.getUsers(), conversation.getStringConversation(), conversation.getDate(), conversation);

                    Node finalNode = node;
                    node.setOnMouseEntered(event -> finalNode.setStyle("-fx-background-color : #0A0E3F"));

                    node.setOnMouseExited(event -> {
                        finalNode.setStyle("-fx-background-color : linear-gradient(to bottom left, #05071F, #431FA0)");
                        finalNode.setStyle("-fx-background-radius: 10");
                    });

                    conversationItems.getChildren().add(node);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (conversationItems.getChildren().size() == 0) {
            HBox hbox = new HBox();
            hbox.setPrefWidth(744);
            hbox.setPrefHeight(350);
            hbox.setAlignment(Pos.CENTER);

            Label text = new Label();
            text.setText("You don't have conversations.");
            text.setStyle("-fx-text-fill: #05071F");
            text.setStyle("-fx-font-size: 20");

            hbox.getChildren().add(text);
            conversationItems.getChildren().add(hbox);
        }

    }

    private Message getReply(Long id, Iterable<Message> messages, Service service) {
        User x = service.getUser(SignInController.currentUser);
        for (var message : messages) {
            if (Objects.equals(message.getReply(), id) && (Objects.equals(message.getFrom().getUsername(), SignInController.currentUser) || message.getTo().contains(x)))
                return message;
        }
        return null;
    }

    public void refresh() {
        initialize();
    }
}

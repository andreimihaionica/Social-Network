package com.example.social_network.controllers;

import com.example.social_network.domain.Message;
import com.example.social_network.domain.User;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AllConversationsController {
    private boolean replyAll = false;
    private boolean isDisableReply = false, isDisableReplyAll = false;

    @FXML
    Pane pnlAllConversations;

    @FXML
    Pane pnlSendNewMessage;

    @FXML
    Pane pnlSeeMessage;

    @FXML
    TextField tfTo;

    @FXML
    TextArea taMessage;

    @FXML
    Button btnSendMessage;

    @FXML
    Label warningMessage;

    @FXML
    VBox conversationItems;

    @FXML
    VBox messages;

    @FXML
    HBox hbox;

    @FXML
    Button reply;

    @FXML
    Button replyall;

    @FXML
    TextArea textArea;

    @FXML
    Button btnSend;

    @FXML
    public void initialize() {
        changePnlAllConversations();
    }

    private Message getReply(Long id, Iterable<Message> messages, Service service) {
        User x = service.getUser(SignInController.currentUser);
        for (var message : messages) {
            if (Objects.equals(message.getReply(), id) && (Objects.equals(message.getFrom().getUsername(), SignInController.currentUser) || message.getTo().contains(x)))
                return message;
        }
        return null;
    }

    public void changePnlAllConversations() {
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

                    node.setOnMouseClicked(e -> seeMessage(conversation));

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

            pnlAllConversations.setVisible(true);
            pnlSendNewMessage.setVisible(false);
            pnlSeeMessage.setVisible(false);

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

    public void changePnlSendNewMessage() {
        tfTo.setText("");
        taMessage.setText("");

        pnlAllConversations.setVisible(false);
        pnlSendNewMessage.setVisible(true);
        pnlSeeMessage.setVisible(false);
    }

    public void sendNewMessage() {
        warningMessage.setTextFill(Color.RED);
        if (tfTo.getText() == null || tfTo.getText().isEmpty())
            warningMessage.setText('"' + "To" + '"' + " field must not be empty!");
        else if (taMessage.getText() == null || taMessage.getText().isEmpty())
            warningMessage.setText('"' + "Message" + '"' + " field must not be empty!");
        else {
            try {
                SignInController.service.sendMessage(SignInController.currentUser, tfTo.getText(), taMessage.getText(), 0L);
                warningMessage.setTextFill(Color.GREEN);
                warningMessage.setText("Message sent!");

                btnSendMessage.setDisable(true);
            } catch (IllegalArgumentException exception) {
                warningMessage.setText(exception.getMessage());
            }
        }
    }

    public void seeMessage(Conversation conversation) {
        messages.getChildren().clear();
        Node node;
        for (Message message : conversation.getConversation()) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/messageItem.fxml"));
                node = fxmlLoader.load();

                MessageItemController controller = fxmlLoader.getController();

                StringBuilder toFrom = new StringBuilder(message.getFrom().getUsername());
                toFrom.append(" to ");
                for (User user : message.getTo())
                    toFrom.append(user.getUsername()).append(", ");
                toFrom = new StringBuilder(toFrom.substring(0, toFrom.length() - 2));

                controller.initialize(String.valueOf(toFrom), message.getMessage(), message.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm:ss")));

                Node finalNode = node;
                node.setOnMouseEntered(event -> finalNode.setStyle("-fx-background-color : #0A0E3F"));

                node.setOnMouseExited(event -> {
                    finalNode.setStyle("-fx-background-color : linear-gradient(to bottom left, #05071F, #431FA0)");
                    finalNode.setStyle("-fx-background-radius: 20");
                });

                messages.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        Message lastMessage = conversation.getConversation().get(conversation.getConversation().size() - 1);

        reply.setDisable(false);
        replyall.setDisable(false);

        isDisableReply = false;
        isDisableReplyAll = false;

        if (Objects.equals(lastMessage.getFrom().getUsername(), SignInController.currentUser) || Objects.equals(lastMessage.getFrom().getUsername(), "Notification")) {
            reply.setDisable(true);
            replyall.setDisable(true);

            isDisableReply = true;
            isDisableReplyAll = true;
        }

        if (lastMessage.getTo().size() == 1) {
            replyall.setDisable(true);
            isDisableReplyAll = true;
        }

        btnSend.setOnAction(e -> send(conversation));
        reply.setOnAction(e -> reply());
        replyall.setOnAction(e -> replyAll());

        pnlAllConversations.setVisible(false);
        pnlSendNewMessage.setVisible(false);
        pnlSeeMessage.setVisible(true);
    }

    public void reply() {
        hbox.setVisible(true);
        replyAll = false;
        reply.setDisable(true);
        if (!isDisableReplyAll) {
            replyall.setDisable(false);
        }
    }

    public void replyAll() {
        hbox.setVisible(true);
        replyAll = true;
        replyall.setDisable(true);
        if (!isDisableReply) {
            reply.setDisable(false);
        }
    }

    public void send(Conversation conversation) {
        if (!textArea.getText().isEmpty() && textArea.getText() != null) {
            Message lastMessage = conversation.getConversation().get(conversation.getConversation().size() - 1);
            if (replyAll) {
                StringBuilder to = new StringBuilder();
                for (User user : lastMessage.getTo())
                    if (!Objects.equals(user.getUsername(), SignInController.currentUser)) {
                        to.append(user.getUsername()).append(";");
                    }
                to.append(lastMessage.getFrom().getUsername());

                Message message = SignInController.service.sendMessage(SignInController.currentUser, to.toString(), textArea.getText(), lastMessage.getId());
                hbox.setVisible(false);
                conversation.getConversation().add(message);
            } else {
                Message message = SignInController.service.sendMessage(SignInController.currentUser, lastMessage.getFrom().getUsername(), textArea.getText(), lastMessage.getId());
                hbox.setVisible(false);
                conversation.getConversation().add(message);
            }
            seeMessage(conversation);
        }
    }

    public void refresh() {
        initialize();
    }
}

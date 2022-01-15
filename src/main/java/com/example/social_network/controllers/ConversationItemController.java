package com.example.social_network.controllers;

import com.example.social_network.util.Conversation;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ConversationItemController {
    Conversation conversation;

    @FXML
    Label lblTo;

    @FXML
    Label lblLastMessage;

    @FXML
    Label lblDate;

    public void initialize(String username, String lastMessage, String date, Conversation conversation) {
        lblTo.setText(username);
        lblLastMessage.setText(lastMessage);
        lblDate.setText(date);
        this.conversation = conversation;
    }
}

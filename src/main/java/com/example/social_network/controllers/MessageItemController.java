package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageItemController {
    @FXML
    Label lblToFrom;

    @FXML
    Label lblMessage;

    @FXML
    Label lblDate;

    public void initialize(String toFrom, String message, String date) {
        lblToFrom.setText(toFrom);
        lblMessage.setText(message);
        lblDate.setText(date);
    }
}

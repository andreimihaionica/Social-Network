package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FriendRequestItemController {
    @FXML
    Label lblUsername;

    @FXML
    Label lblDate;

    public void initialize(String username, String date) {
        lblUsername.setText(username);
        lblDate.setText(date);
    }
}

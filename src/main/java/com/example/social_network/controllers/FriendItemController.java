package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FriendItemController {
    @FXML
    Label lblUsername;

    @FXML
    Label lblDate;

    @FXML
    Button btnRemove;

    public void initialize(String username, String date) {
        lblUsername.setText(username);
        lblDate.setText(date);
    }

    public String username() {
        return lblUsername.getText();
    }

    public Button getBtnRemove() {
        return btnRemove;
    }
}

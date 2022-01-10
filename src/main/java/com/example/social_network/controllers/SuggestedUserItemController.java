package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class SuggestedUserItemController {
    @FXML
    Label lblUsername;

    @FXML
    Label lblMutualFriends;

    @FXML
    Button btnAddFriend;

    @FXML
    ImageView iconBtn;

    public void initialize(String username, Integer mutualFriends) {
        lblUsername.setText(username);
        lblMutualFriends.setText(mutualFriends.toString() + " mutual friends");
    }

    public String username() {
        return lblUsername.getText();
    }

    public Button getBtnAddFriend() {
        return btnAddFriend;
    }

    public ImageView getIconBtn() {
        return iconBtn;
    }

    public void setIconBtn(ImageView imageView) {
        iconBtn = imageView;
    }
}

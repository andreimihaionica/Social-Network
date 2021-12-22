package com.example.social_network.util;

import javafx.scene.control.Button;

public class SearchUser {
    private String username;
    private Button button;

    public SearchUser(String username) {
        this.username = username;
        this.button = new Button(" ");
    }

    public String getUsername() {
        return username;
    }

    public Button getButton() {
        return button;
    }
}

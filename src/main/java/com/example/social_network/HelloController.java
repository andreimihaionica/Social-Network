package com.example.social_network;

import com.example.social_network.service.Service;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {
    private Service service;

    public void setService(Service service1) {
        this.service = service1;
    }

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    protected void logIn() {
        if(service.getUser(usernameField.getText()) != null)
            System.out.println("LOGARE CU SUCCES!");
        else
            System.out.println("Nu-i ok");
    }
}
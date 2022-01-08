package com.example.social_network.controllers;

import com.example.social_network.factory.Factory;
import com.example.social_network.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LogInController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final Factory factory = new Factory();
    public static Service service = factory.getService();

    public static String currentUser;

    @FXML
    TextField usernameField;

    @FXML
    Label warningMessage;

    public void logIn(ActionEvent event) throws IOException {
        if (service.getUser(usernameField.getText()) != null) {
            currentUser = usernameField.getText();

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/socialNetwork.fxml")));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            warningMessage.setText("Incorrect username!");
        }
    }
}

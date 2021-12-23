package com.example.social_network;

import com.example.social_network.factory.Factory;
import com.example.social_network.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SendNewMessageController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private static final Factory factory = new Factory();
    public static Service service = factory.getService();

    public static String currentUser;

    @FXML
    TextField to;

    @FXML
    TextArea message;

    @FXML
    Label warningMessage;

    @FXML
    public void initialize() {
        currentUser = LogInController.currentUser;
        service = LogInController.service;
    }

    public void sendMessage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if(to.getText() == null || to.getText().isEmpty())
           warningMessage.setText('"' + "To" + '"' + " field must not be empty!");
        else if(message.getText() == null || message.getText().isEmpty())
            warningMessage.setText('"' + "Message" + '"' + " field must not be empty!");
        else{
            try {
                service.sendMessage(currentUser, to.getText(), message.getText(), 0L);
            } catch (IllegalArgumentException exception) {
                warningMessage.setText(exception.getMessage());
            }
        }

        if(warningMessage.getText().isEmpty()){
            stage.close();
        }
    }
}

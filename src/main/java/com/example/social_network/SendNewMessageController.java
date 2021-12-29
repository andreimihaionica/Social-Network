package com.example.social_network;

import com.example.social_network.domain.Message;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SendNewMessageController {
    private Stage stage;

    private Service service;
    private ObservableList<Conversation> data;

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

    public void setService(Service service, ObservableList<Conversation> data) {
        this.service = service;
        this.data = data;
    }

    public void sendMessage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if(to.getText() == null || to.getText().isEmpty())
           warningMessage.setText('"' + "To" + '"' + " field must not be empty!");
        else if(message.getText() == null || message.getText().isEmpty())
            warningMessage.setText('"' + "Message" + '"' + " field must not be empty!");
        else{
            try {
                Message msg = service.sendMessage(currentUser, to.getText(), message.getText(), 0L);
                List<Message> msgList = new ArrayList<>();
                msgList.add(msg);
                Conversation conversation = new Conversation(msgList, currentUser);
                MessagesController.data.add(conversation);
            } catch (IllegalArgumentException exception) {
                warningMessage.setText(exception.getMessage());
            }
        }

        if(warningMessage.getText().isEmpty()){
            stage.close();
        }
    }
}

package com.example.social_network.controllers;

import com.example.social_network.domain.Message;
import com.example.social_network.domain.User;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import com.example.social_network.util.MessageString;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SeeMessageController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private boolean replyAll = false;
    private boolean isDisableReply = false, isDisableReplyAll = false;
    private ObservableList<MessageString> messages;
    private ObservableList<Conversation> data;
    private int row;
    public static String currentUser;
    private static Service service;

    @FXML
    TableView tableMessage;

    @FXML
    TableColumn fromColumn;

    @FXML
    TableColumn toColumn;

    @FXML
    TableColumn messageColumn;

    @FXML
    TableColumn dateColumn;

    @FXML
    HBox hbox;

    @FXML
    Button reply;

    @FXML
    Button replyall;

    @FXML
    TextArea textArea;

    @FXML
    public void initialize() {
        currentUser = LogInController.currentUser;
        service = LogInController.service;
        row = MessagesController.row;

        data = MessagesController.data;
        if (tableMessage != null) {
            tableMessage.setPlaceholder(new Label("ERROR 404."));
            fromColumn.setCellValueFactory(new PropertyValueFactory<MessageString, String>("from"));
            toColumn.setCellValueFactory(new PropertyValueFactory<MessageString, String>("to"));
            messageColumn.setCellValueFactory(new PropertyValueFactory<MessageString, String>("message"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<MessageString, String>("date"));

            fromColumn.setStyle("-fx-alignment: CENTER;");
            toColumn.setStyle("-fx-alignment: CENTER;");
            messageColumn.setStyle("-fx-alignment: CENTER;");
            dateColumn.setStyle("-fx-alignment: CENTER;");

            messages = FXCollections.observableArrayList();

            for (Message message : data.get(row).getConversation()) {
                String to = "";
                for (User user : message.getTo())
                    to = to + user.getUsername() + ";";

                to = to.substring(0, to.length() - 1);

                messages.add(new MessageString(message.getFrom().getUsername(), to, message.getMessage(), message.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm"))));
            }
            tableMessage.setItems(messages);
        }

        Message lastMessage = data.get(row).getConversation().get(data.get(row).getConversation().size() - 1);
        if (Objects.equals(lastMessage.getFrom().getUsername(), currentUser)) {
            reply.setDisable(true);
            replyall.setDisable(true);

            isDisableReply = true;
            isDisableReplyAll = true;
        }

        if (lastMessage.getTo().size() == 1) {
            replyall.setDisable(true);
            isDisableReplyAll = true;
        }
    }

    public void reply() {
        hbox.setVisible(true);
        replyAll = false;
        reply.setDisable(true);
        if (!isDisableReplyAll) {
            replyall.setDisable(false);
        }
    }

    public void replyAll() {
        hbox.setVisible(true);
        replyAll = true;
        replyall.setDisable(true);
        if (!isDisableReply) {
            reply.setDisable(false);
        }
    }

    public void send() {
        if (!textArea.getText().isEmpty() && textArea.getText() != null) {
            Message lastMessage = data.get(row).getConversation().get(data.get(row).getConversation().size() - 1);
            if (replyAll) {
                String to = "";
                for (User user : lastMessage.getTo())
                    if (!Objects.equals(user.getUsername(), currentUser)) {
                        to = to + user.getUsername() + ";";
                    }
                to = to + lastMessage.getFrom().getUsername();
                Message messageSent = service.sendMessage(currentUser, to, textArea.getText(), lastMessage.getId());
                hbox.setVisible(false);
                messages.add(new MessageString(currentUser, to, messageSent.getMessage(), messageSent.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm"))));
                data.get(row).getConversation().add(messageSent);
            } else {
                Message messageSent = service.sendMessage(currentUser, lastMessage.getFrom().getUsername(), textArea.getText(), lastMessage.getId());
                hbox.setVisible(false);
                messages.add(new MessageString(currentUser, lastMessage.getFrom().getUsername(), messageSent.getMessage(), messageSent.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy HH:mm"))));
                data.get(row).getConversation().add(messageSent);
            }
            initialize();
        }
    }

    public void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/receivedMessages.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

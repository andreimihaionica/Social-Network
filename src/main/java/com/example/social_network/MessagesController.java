package com.example.social_network;

import com.example.social_network.domain.Message;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import com.example.social_network.util.FriendRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MessagesController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static String currentUser;
    private static Service service;

    @FXML
    TableView tableReceivedMessages;

    @FXML
    TableColumn usersColumn;

    @FXML
    TableColumn conversationColumn;


    @FXML
    public void initialize() {
        currentUser = LogInController.currentUser;
        service = LogInController.service;

        if (tableReceivedMessages != null) {
            tableReceivedMessages.setPlaceholder(new Label("You don't have any messages."));
            usersColumn.setCellValueFactory(new PropertyValueFactory<Conversation, String>("users"));
            conversationColumn.setCellValueFactory(new PropertyValueFactory<Conversation, String>("stringConversation"));

            usersColumn.setStyle("-fx-alignment: CENTER;");
            conversationColumn.setStyle("-fx-alignment: CENTER;");

            tableReceivedMessages.setItems(getConversations());
        }
    }

    private Message getReply(Long id){
        for(var message:service.getAllMessages()) {
            if(Objects.equals(message.getReply(), id) && (Objects.equals(message.getFrom().getUsername(), currentUser) || message.getTo().contains(service.getUser(currentUser))))
                return message;
        }
        return null;
    }

    private ObservableList<Conversation> getConversations() {
        ObservableList<Conversation> data = FXCollections.observableArrayList();

        List<Message> messages = new ArrayList<>();
        for (Message message : service.getAllMessages()) {
            messages.add(message);
        }
        messages.sort(Comparator.comparing(Message::getDate));

        for (Message message : messages) {
            if (((Objects.equals(message.getFrom().getUsername(), currentUser)) ||
                    (message.getTo().contains(service.getUser(currentUser))))
                    && message.getReply() == 0L) {
                List<Message> messageList = new ArrayList<>();

                Message message1 = message;
                messageList.add(message1);

                while (getReply(message1.getId()) != null) {
                    message1 = getReply(message1.getId());
                    messageList.add(message1);
                }

                Conversation conversation = new Conversation(messageList);
                data.add(conversation);
            }
        }

        return data;
    }
    public void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("socialNetwork.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

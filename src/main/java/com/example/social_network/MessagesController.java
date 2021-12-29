package com.example.social_network;

import com.example.social_network.domain.Message;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MessagesController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public static ObservableList<Conversation> data;
    public static int row;
    public static String currentUser;
    private static Service service;

    @FXML
    TableView tableReceivedMessages;

    @FXML
    TableColumn usersColumn;

    @FXML
    TableColumn conversationColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = LogInController.currentUser;
        service = LogInController.service;

        if (tableReceivedMessages != null) {
            tableReceivedMessages.setPlaceholder(new Label("You don't have any messages."));
            usersColumn.setCellValueFactory(new PropertyValueFactory<Conversation, String>("users"));
            conversationColumn.setCellValueFactory(new PropertyValueFactory<Conversation, String>("stringConversation"));

            usersColumn.setStyle("-fx-alignment: CENTER;");
            conversationColumn.setStyle("-fx-alignment: CENTER;");

            getConversations();
            tableReceivedMessages.setItems(data);
        }

        tableReceivedMessages.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = tableReceivedMessages.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                int val = tablePosition.getRow();
                seeMessage(val);
            }
        });
    }

    private Message getReply(Long id){
        for(var message:service.getAllMessages()) {
            if(Objects.equals(message.getReply(), id) && (Objects.equals(message.getFrom().getUsername(), currentUser) || message.getTo().contains(service.getUser(currentUser))))
                return message;
        }
        return null;
    }

    private void getConversations() {
        data = FXCollections.observableArrayList();

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

                Conversation conversation = new Conversation(messageList, currentUser);
                data.add(conversation);
            }
        }
    }

    public void sendNewMessage(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sendNewMessage.fxml"));
        root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Send new message");
        stage.setScene(new Scene(root));
        SendNewMessageController sendNewMessageController = fxmlLoader.getController();
        sendNewMessageController.setService(service, data);
        stage.show();
    }

    public void seeMessage(int row) {
        MessagesController.row = row;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("seeMessage.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage) tableReceivedMessages.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("socialNetwork.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

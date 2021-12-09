package com.example.social_network;

import com.example.social_network.service.Service;
//import com.example.social_network.util.FriendRequest;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.List;

public class HelloController {
    private Service service;
    String currentUser;

    public void setService(Service service1) {
        this.service = service1;
    }

    @FXML
    VBox vBox;

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

    @FXML
    TableView tableFriendRequests;

    public static class FriendRequest {
        public final String username;
        public final String date;
        public final Button accept, reject;

        public FriendRequest(String username1, String date1) {
            username = username1;
            date = date1;
            accept = new Button("Accept");
            reject = new Button("Reject");
        }

        public final String getUsername() {
            return username;
        }
        public final String getDate() {
            return date;
        }
        public final Button getAccept() {
            return accept;
        }
        public final Button getReject() {
            return reject;
        }
    }

    @FXML
    protected void initializeTableFriendRequests() {
        TableColumn usernameCol = new TableColumn("Username");
        TableColumn dateCol = new TableColumn("Date");
        TableColumn acceptCol = new TableColumn(" ");
        TableColumn rejectCol = new TableColumn(" ");

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setStyle("-fx-alignment: CENTER;");

        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setStyle("-fx-alignment: CENTER;");

        acceptCol.setCellValueFactory(new PropertyValueFactory<>("accept"));
        acceptCol.setStyle("-fx-alignment: CENTER;");

        rejectCol.setCellValueFactory(new PropertyValueFactory<>("reject"));
        rejectCol.setStyle("-fx-alignment: CENTER;");

        tableFriendRequests.getColumns().addAll(usernameCol, dateCol, acceptCol, rejectCol);
    }

    @FXML
    protected void showFriendRequests() {
        ObservableList<FriendRequest> data = FXCollections.observableArrayList();

        String username, date;
        List<String> split;

        for(String friendRequest:service.getAllPendingFriendships(currentUser)){
            split = List.of(friendRequest.split(" "));
            username = split.get(0);
            date = split.get(2);

            FriendRequest friendRequest1 = new FriendRequest(username, date);
            friendRequest1.getAccept().setOnAction(e -> {
                service.updateFriendshipStatus(currentUser, friendRequest1.getUsername(), "APPROVED");
                tableFriendRequests.getItems().remove(friendRequest1);
            });
            friendRequest1.getReject().setOnAction(e -> {
                service.updateFriendshipStatus(currentUser, friendRequest1.getUsername(), "REJECTED");
                tableFriendRequests.getItems().remove(friendRequest1);
            });

            data.add(friendRequest1);
        }

        tableFriendRequests.setItems(data);
    }

    @FXML
    protected void logIn() {
        if(service.getUser(usernameField.getText()) != null){
            currentUser = usernameField.getText();

            vBox.getChildren().removeAll(vBox.getChildren());

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(30);

            VBox vBoxLeft = new VBox();
            vBoxLeft.setAlignment(Pos.CENTER);
            vBoxLeft.setSpacing(10);

            VBox vBoxRight = new VBox();
            vBoxRight.setAlignment(Pos.CENTER);
            vBoxRight.setSpacing(10);

            TextField searchUser = new TextField();
            TableView tableUsers = new TableView();
            vBoxLeft.getChildren().addAll(searchUser, tableUsers);

            Button showFriendRequestsButton = new Button("Show friend requests");
            showFriendRequestsButton.setOnAction(e -> {
                showFriendRequests();
            });
            tableFriendRequests = new TableView<FriendRequest>();
            initializeTableFriendRequests();
            vBoxRight.getChildren().addAll(showFriendRequestsButton, tableFriendRequests);

            hBox.getChildren().addAll(vBoxLeft, vBoxRight);
            vBox.getChildren().add(hBox);
        }
        else{
            Label mesaj = new Label("Incorrect username!");
            mesaj.setFont(new Font("System", 15));
            vBox.getChildren().add(mesaj);
        }
    }
}
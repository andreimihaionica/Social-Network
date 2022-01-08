package com.example.social_network.controllers;

import com.example.social_network.service.Service;
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
import java.util.List;
import java.util.Objects;

public class FriendRequestsController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public static String currentUser;
    private static Service service;

    @FXML
    TableView tableFriendRequests;

    @FXML
    TableColumn userColumn;

    @FXML
    TableColumn dateColumn;

    @FXML
    TableColumn acceptColumn;

    @FXML
    TableColumn rejectColumn;

    @FXML
    public void initialize() {
        currentUser = LogInController.currentUser;
        service = LogInController.service;

        if (tableFriendRequests != null) {
            tableFriendRequests.setPlaceholder(new Label("You don't have any friend request."));
            userColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("username"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("date"));
            acceptColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, Button>("accept"));
            rejectColumn.setCellValueFactory(new PropertyValueFactory<FriendRequest, Button>("reject"));

            userColumn.setStyle("-fx-alignment: CENTER;");
            dateColumn.setStyle("-fx-alignment: CENTER;");
            acceptColumn.setStyle("-fx-alignment: CENTER;");
            rejectColumn.setStyle("-fx-alignment: CENTER;");

            tableFriendRequests.setItems(searchFriendRequests());
        }

    }

    private ObservableList<FriendRequest> searchFriendRequests() {
        ObservableList<FriendRequest> data = FXCollections.observableArrayList();

        String username, date;
        List<String> split;

        for (String friendRequest : service.getAllPendingFriendships(currentUser)) {
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

        return data;
    }
    public void goBack(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/socialNetwork.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

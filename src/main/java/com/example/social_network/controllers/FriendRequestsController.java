package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class FriendRequestsController {
    @FXML
    VBox friendRequestItems;

    @FXML
    public void initialize() {
        friendRequestItems.getChildren().clear();

        Node node;
        FriendRequestItemController controller;

        List<String> split;
        String username, date;

        for (String friendRequest : SignInController.service.getAllPendingFriendships(SignInController.currentUser)) {
            try {
                split = List.of(friendRequest.split(" "));
                username = split.get(0);
                date = split.get(2);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/friendRequest.fxml"));
                node = fxmlLoader.load();

                controller = fxmlLoader.getController();
                controller.initialize(username, date);

                String finalUsername = username;
                Node finalNode = node;
                controller.getBtnAccept().setOnAction(e -> {
                    SignInController.service.updateFriendshipStatus(SignInController.currentUser, finalUsername, "APPROVED");
                    refresh();
                });

                controller.getBtnReject().setOnAction(e -> {
                    SignInController.service.updateFriendshipStatus(SignInController.currentUser, finalUsername, "REJECTED");
                    refresh();
                });

                node.setOnMouseEntered(event -> finalNode.setStyle("-fx-background-color : #0A0E3F"));

                node.setOnMouseExited(event -> {
                    finalNode.setStyle("-fx-background-color : linear-gradient(to bottom left, #05071F, #431FA0)");
                    finalNode.setStyle("-fx-background-radius: 10");
                });

                friendRequestItems.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(friendRequestItems.getChildren().size() == 0) {
            HBox hbox = new HBox();
            hbox.setPrefWidth(744);
            hbox.setPrefHeight(350);
            hbox.setAlignment(Pos.CENTER);

            Label text = new Label();
            text.setText("You don't have any friend requests.");
            text.setStyle("-fx-text-fill: #05071F");
            text.setStyle("-fx-font-size: 20");

            hbox.getChildren().add(text);
            friendRequestItems.getChildren().add(hbox);
        }
    }

    public void refresh() {
        initialize();
    }

}


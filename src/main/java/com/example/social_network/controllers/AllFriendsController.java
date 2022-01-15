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

public class AllFriendsController {
    @FXML
    VBox friendItems;

    @FXML
    public void initialize() {
        friendItems.getChildren().clear();

        Node node;
        FriendItemController controller;

        List<String> split;
        String username, date;


        for (String friend : SignInController.service.getAllFriendsForUser(SignInController.currentUser)) {
            try {
                split = List.of(friend.split(" "));
                username = split.get(0);
                date = split.get(2);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/friendItem.fxml"));
                node = fxmlLoader.load();

                controller = fxmlLoader.getController();
                controller.initialize(username, date);

                String finalUsername = username;
                Node finalNode = node;
                controller.getBtnRemove().setOnAction(e -> {
                    SignInController.service.deleteFriendship(SignInController.currentUser, finalUsername);
                    friendItems.getChildren().remove(finalNode);
                });

                node.setOnMouseEntered(event -> finalNode.setStyle("-fx-background-color : #0A0E3F"));

                node.setOnMouseExited(event -> {
                    finalNode.setStyle("-fx-background-color : linear-gradient(to bottom left, #05071F, #431FA0)");
                    finalNode.setStyle("-fx-background-radius: 10");
                });

                friendItems.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (friendItems.getChildren().size() == 0) {
            HBox hbox = new HBox();
            hbox.setPrefWidth(744);
            hbox.setPrefHeight(350);
            hbox.setAlignment(Pos.CENTER);

            Label text = new Label();
            text.setText("You don't have any friends.");
            text.setStyle("-fx-text-fill: #05071F");
            text.setStyle("-fx-font-size: 20");

            hbox.getChildren().add(text);
            friendItems.getChildren().add(hbox);
        }
    }

    public void refresh() {
        initialize();
    }
}

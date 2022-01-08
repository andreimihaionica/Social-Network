package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    StackPane stackPane;

    @FXML
    Pane friendRequests;

    @FXML
    VBox friendRequestItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Node[] nodes = new Node[10];
        List<String> users = new ArrayList<>();
        users.add("Andrei");
        users.add("Lucia");
        users.add("Vasile");
        users.add("Maria");
        users.add("Gheorghe");
        users.add("Marian");
        users.add("Teodora");
        users.add("Mara");
        users.add("Bogdan");
        users.add("Teodor");

        FriendRequestItemController controller;
        for (int i = 0; i < nodes.length; i++) {
            try {

                final int j = i;
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/friendRequest.fxml"));
                nodes[i] = fxmlLoader.load();

                controller = fxmlLoader.getController();
                controller.initialize(users.get(i), "11.12.2022");


                //give the items some effect

                nodes[i].setOnMouseEntered(event -> {
                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
                });

                nodes[i].setOnMouseExited(event -> {
                    nodes[j].setStyle("-fx-background-color : linear-gradient(to bottom left, #05071F, #431FA0)");
                    nodes[j].setStyle("-fx-background-radius: 10");
                });

                friendRequestItems.getChildren().add(nodes[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

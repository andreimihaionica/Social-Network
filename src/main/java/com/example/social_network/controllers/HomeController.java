package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.Objects;

public class HomeController {
    @FXML
    StackPane stackPane;

    @FXML
    Pane pnlOverview;

    @FXML
    Pane pnlMessages;

    @FXML
    Pane pnlFriendRequests;

    @FXML
    Pane pnlSugestions;

    @FXML
    Pane pnlAllFriends;

    @FXML
    Pane pnlEvents;

    @FXML
    Pane pnlStatistics;

    @FXML
    public void initialize() {

    }

    public void changePaneToOverview() {
        pnlOverview.setVisible(true);
        pnlMessages.setVisible(false);
        pnlFriendRequests.setVisible(false);
        pnlSugestions.setVisible(false);
        pnlAllFriends.setVisible(false);
        pnlEvents.setVisible(false);
        pnlStatistics.setVisible(false);
    }

    public void changePaneToFriendRequests() throws IOException {
        pnlFriendRequests.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/friendRequests.fxml"))));

        pnlOverview.setVisible(false);
        pnlMessages.setVisible(false);
        pnlFriendRequests.setVisible(true);
        pnlSugestions.setVisible(false);
        pnlAllFriends.setVisible(false);
        pnlEvents.setVisible(false);
        pnlStatistics.setVisible(false);
    }
}

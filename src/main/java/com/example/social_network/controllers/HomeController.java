package com.example.social_network.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

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
    Circle btnClose;

    @FXML
    Label lblCurrentUser;

    @FXML
    public void initialize() throws IOException {
        lblCurrentUser.setText(SignInController.currentUser);
        changePaneToOverview();
        SignInController.service.sendNotification(SignInController.currentUser);
    }

    public void changePaneToOverview() throws IOException {
        pnlOverview.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/overview.fxml"))));

        pnlOverview.setVisible(true);
        pnlMessages.setVisible(false);
        pnlFriendRequests.setVisible(false);
        pnlSugestions.setVisible(false);
        pnlAllFriends.setVisible(false);
        pnlEvents.setVisible(false);
        pnlStatistics.setVisible(false);
    }

    public void changePaneToMessages() throws IOException {
        pnlOverview.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/messages.fxml"))));

        pnlOverview.setVisible(false);
        pnlMessages.setVisible(true);
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

    public void changePaneToAllFriends() throws IOException {
        pnlAllFriends.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/allFriends.fxml"))));

        pnlOverview.setVisible(false);
        pnlMessages.setVisible(false);
        pnlFriendRequests.setVisible(false);
        pnlSugestions.setVisible(false);
        pnlAllFriends.setVisible(true);
        pnlEvents.setVisible(false);
        pnlStatistics.setVisible(false);
    }

    public void changePaneToStatistics() throws IOException {
        pnlStatistics.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/statistics.fxml"))));

        pnlOverview.setVisible(false);
        pnlMessages.setVisible(false);
        pnlFriendRequests.setVisible(false);
        pnlSugestions.setVisible(false);
        pnlAllFriends.setVisible(false);
        pnlEvents.setVisible(false);
        pnlStatistics.setVisible(true);
    }

    public void changePaneToEvents() throws IOException {
        pnlEvents.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/events.fxml"))));

        pnlOverview.setVisible(false);
        pnlMessages.setVisible(false);
        pnlFriendRequests.setVisible(false);
        pnlSugestions.setVisible(false);
        pnlAllFriends.setVisible(false);
        pnlEvents.setVisible(true);
        pnlStatistics.setVisible(false);
    }

    public void signOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/signIn.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}

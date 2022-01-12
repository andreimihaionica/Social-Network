package com.example.social_network.controllers;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.FriendshipStatus;
import com.example.social_network.domain.User;
import com.example.social_network.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.*;

public class OverviewController {
    Service service;

    @FXML
    VBox suggestedUserItems;

    @FXML
    TextField searchUser;

    @FXML
    Label lblText;

    @FXML
    public void initialize() throws IOException {
        service = SignInController.service;
        suggestedUserItems.getChildren().clear();

        int noSuggestions = 5;

        Iterable<Friendship> friendships = service.getAllFriendships();
        Iterable<User> users = service.getAllUsers();

        for (User user : users) {
            try {
                if (!Objects.equals(SignInController.currentUser, user.getUsername()) && getFriendship(SignInController.currentUser, user.getUsername(), users, friendships) == null) {
                    suggestedUserItems.getChildren().add(getNode(user.getUsername(), false));

                    noSuggestions--;
                    if (noSuggestions == 0)
                        break;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        verify();
    }

    public Friendship getFriendship(String username1, String username2, Iterable<User> users, Iterable<Friendship> friendships) {
        User user1 = null, user2 = null;
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username1))
                user1 = user;

            if (Objects.equals(user.getUsername(), username2))
                user2 = user;
        }

        if (user1 != null && user2 != null) {
            for (Friendship friendship : friendships) {
                if (Objects.equals(friendship.getId().getLeft(), user1.getId()) && Objects.equals(friendship.getId().getRight(), user2.getId()))
                    return friendship;

                if (Objects.equals(friendship.getId().getRight(), user1.getId()) && Objects.equals(friendship.getId().getLeft(), user2.getId()))
                    return friendship;
            }
        }

        return null;
    }

    public void searchUser(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            refresh();
        }
    }

    public void refresh() {
        suggestedUserItems.getChildren().clear();
        lblText.setText("Results:");

        int noSuggestions = 5;

        for (User user : service.getAllUsers()) {
            try {
                if (user.getUsername().startsWith(searchUser.getText()) && !Objects.equals(user.getUsername(), SignInController.currentUser) && service.verifyFriendship(SignInController.currentUser, user.getUsername()) != FriendshipStatus.REJECTED) {
                    suggestedUserItems.getChildren().add(getNode(user.getUsername(), true));
                    noSuggestions--;
                    if (noSuggestions == 0)
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        verify();
    }

    public Node getNode(String username, boolean refresh) throws IOException {
        Node node;
        SuggestedUserItemController controller;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/suggestedUserItem.fxml"));
        node = fxmlLoader.load();

        controller = fxmlLoader.getController();
        controller.initialize(username, new Random().nextInt(12 - 2 + 1) + 2);

        Node finalNode = node;

        Friendship friendship = service.getFriendship(SignInController.currentUser, username);

        if (friendship == null) {
            controller.getBtnAddFriend().setText("Add Friend");
            if (refresh) {
                controller.getBtnAddFriend().setOnAction(e -> {
                    service.addFriendship(SignInController.currentUser, username);
                    refresh();
                });
            } else {
                controller.getBtnAddFriend().setOnAction(e -> {
                    service.addFriendship(SignInController.currentUser, username);
                    suggestedUserItems.getChildren().remove(finalNode);
                });
            }
        } else {
            if (friendship.getStatus() == FriendshipStatus.APPROVED) {
                controller.getBtnAddFriend().setText("Remove Friend");
                controller.getIconBtn().setImage(new Image("file:src/main/resources/icons/denied.png"));

                if (refresh) {
                    controller.getBtnAddFriend().setOnAction(e -> {
                        service.deleteFriendship(SignInController.currentUser, username);
                        refresh();
                    });
                } else {
                    controller.getBtnAddFriend().setOnAction(e -> {
                        service.deleteFriendship(SignInController.currentUser, username);
                        suggestedUserItems.getChildren().remove(finalNode);
                    });
                }

            } else {
                if (friendship.getStatus() == FriendshipStatus.PENDING) {
                    controller.getBtnAddFriend().setText("Cancel Request");
                    controller.getIconBtn().setImage(new Image("file:src/main/resources/icons/cancel.png"));

                    if (refresh) {
                        controller.getBtnAddFriend().setOnAction(e -> {
                            service.deleteFriendship(SignInController.currentUser, username);
                            refresh();
                        });
                    } else {
                        controller.getBtnAddFriend().setOnAction(e -> {
                            service.deleteFriendship(SignInController.currentUser, username);
                            suggestedUserItems.getChildren().remove(finalNode);
                        });
                    }

                }
            }
        }

        node.setOnMouseEntered(event -> finalNode.setStyle("-fx-background-color : #0A0E3F"));

        node.setOnMouseExited(event -> {
            finalNode.setStyle("-fx-background-color : linear-gradient(to bottom left, #05071F, #431FA0)");
            finalNode.setStyle("-fx-background-radius: 10");
        });

        return node;
    }

    public void verify() {
        if (suggestedUserItems.getChildren().size() == 0) {
            HBox hbox = new HBox();
            hbox.setPrefWidth(744);
            hbox.setPrefHeight(350);
            hbox.setAlignment(Pos.CENTER);

            Label text = new Label();
            text.setText("You don't have any suggestions.");
            text.setStyle("-fx-text-fill: #05071F");
            text.setStyle("-fx-font-size: 20");

            hbox.getChildren().add(text);
            suggestedUserItems.getChildren().add(hbox);
        }
    }

}

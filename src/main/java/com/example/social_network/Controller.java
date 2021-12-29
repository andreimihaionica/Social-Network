package com.example.social_network;

import com.example.social_network.domain.FriendshipStatus;
import com.example.social_network.domain.User;
import com.example.social_network.service.Service;
import com.example.social_network.util.Conversation;
import com.example.social_network.util.SearchUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Service service;

    ObservableList<SearchUser> users = FXCollections.observableArrayList();

    public static String currentUser;

    @FXML
    MenuButton myMenuButton;

    @FXML
    TextField searchString;

    @FXML
    TableColumn<SearchUser, String> usernameColumn;

    @FXML
    TableColumn<SearchUser, Button> buttonColumn;

    @FXML
    TableView tableUsers;

    @FXML
    ListView listFriends;

    @FXML
    public void initialize() {
        currentUser = LogInController.currentUser;
        service = LogInController.service;

        if (listFriends != null) {
            listFriends.setPlaceholder(new Label("You don't have any friends."));
            Iterable<User> allUsers = service.getAllUsers();
            for (User user : allUsers) {
                if (service.verifyFriendship(user.getUsername(), currentUser) == FriendshipStatus.APPROVED) {
                    listFriends.getItems().add(user.getUsername());
                }
            }
        }

        if (myMenuButton != null) {
            myMenuButton.setText(currentUser);
        }

        if (tableUsers != null) {
            tableUsers.setPlaceholder(new Label("No user found."));
            usernameColumn.setCellValueFactory(new PropertyValueFactory<SearchUser, String>("username"));
            buttonColumn.setCellValueFactory(new PropertyValueFactory<SearchUser, Button>("button"));

            usernameColumn.setStyle("-fx-alignment: CENTER;");
            buttonColumn.setStyle("-fx-alignment: CENTER;");

            tableUsers.setItems(searchUser());
        }

        searchString.textProperty().addListener(e -> handleFilter());
    }

    private void handleFilter() {
        Predicate<SearchUser> predicate = u -> u.getUsername().startsWith(searchString.getText());
        users.setAll(searchUser().stream().filter(predicate).collect(Collectors.toList()));
    }

    public ObservableList<SearchUser> searchUser() {
        users.clear();
        Iterable<User> allUsers = service.getAllUsers();
        for (var user : allUsers) {
            SearchUser searchUser = new SearchUser(user.getUsername());
            FriendshipStatus friendshipStatus = service.verifyFriendship(user.getUsername(), currentUser);

            if (friendshipStatus == FriendshipStatus.APPROVED) {
                searchUser.getButton().setText("Remove");
                searchUser.getButton().setOnAction(e -> {
                    service.deleteFriendship(user.getUsername(), currentUser);
                    sceneRefresh();
                });
                users.add(searchUser);

            } else if (!Objects.equals(user.getUsername(), currentUser)) {
                if (friendshipStatus == null) {
                    searchUser.getButton().setText("Add");
                    searchUser.getButton().setOnAction(e -> {
                        service.addFriendship(currentUser, user.getUsername());
                        sceneRefresh();
                    });
                    users.add(searchUser);
                } else if (service.getFriendship(currentUser, user.getUsername()) != null && service.getFriendship(currentUser, user.getUsername()).getStatus() == FriendshipStatus.PENDING) {
                    searchUser.getButton().setText("Cancel");
                    searchUser.getButton().setOnAction(e -> {
                        service.deleteFriendship(currentUser, user.getUsername());
                        sceneRefresh();
                    });
                    users.add(searchUser);
                }
            }
        }
        Collections.sort(users, Comparator.comparing(SearchUser::getUsername));
        return users;
    }

    private void sceneRefresh() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("socialNetwork.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage = (Stage) myMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void logOut() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("logIn.fxml")));
        stage = (Stage) myMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void friendRequests() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("friendRequests.fxml")));
        stage = (Stage) myMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void messages() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("receivedMessages.fxml"));
        root = fxmlLoader.load();
        stage = (Stage) myMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

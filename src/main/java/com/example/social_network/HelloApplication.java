package com.example.social_network;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.Message;
import com.example.social_network.domain.Tuple;
import com.example.social_network.domain.User;
import com.example.social_network.domain.validators.FriendshipValidator;
import com.example.social_network.domain.validators.MessageValidator;
import com.example.social_network.domain.validators.UserValidator;
import com.example.social_network.domain.validators.Validator;
import com.example.social_network.repository.FriendshipDB;
import com.example.social_network.repository.MessageDB;
import com.example.social_network.repository.Repository;
import com.example.social_network.repository.UserDB;
import com.example.social_network.service.FriendshipService;
import com.example.social_network.service.MessageService;
import com.example.social_network.service.Service;
import com.example.social_network.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 600);

        Validator<User> userValidator = new UserValidator();
        Repository<Long, User> userRepository = new UserDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", userValidator);
        // Repository<Long, User> userRepository = new UserDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "password", userValidator);
        UserService userService = new UserService(userRepository);

        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", friendshipValidator);
        // Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "password", friendshipValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);

        Validator<Message> messageValidator = new MessageValidator();
        Repository<Long, Message> messageRepository = new MessageDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", messageValidator);
        // Repository<Long, Message> messageRepository = new MessageDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "password", messageValidator);
        MessageService messageService = new MessageService(messageRepository);

        HelloController ctrl = fxmlLoader.getController();
        ctrl.setService(new Service(userService, friendshipService, messageService));

        stage.setTitle("SocialNetwork");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
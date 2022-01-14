package com.example.social_network;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/allEvents.fxml")));
        Scene scene = new Scene(root);

        stage.getIcons().add(new Image("file:src/main/resources/icons/icon.png"));
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Social Network");

        movableStage(root, stage);

        stage.setScene(scene);
        stage.show();
    }

    private static double xOffset = 0;
    private static double yOffset = 0;

    public static void movableStage(Parent root, Stage stage) {
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}

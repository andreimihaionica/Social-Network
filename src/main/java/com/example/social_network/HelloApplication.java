package com.example.social_network;

import com.example.social_network.factory.Factory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Factory factory = new Factory();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene1 = new Scene(fxmlLoader.load(), 800, 600);

        HelloController ctrl1 = fxmlLoader.getController();
        ctrl1.setService(factory.getService());

        stage.setScene(scene1);
        stage.setTitle("SocialNetwork");
        stage.show();
    }


}
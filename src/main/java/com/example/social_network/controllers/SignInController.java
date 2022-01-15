package com.example.social_network.controllers;

import com.example.social_network.Application;
import com.example.social_network.domain.Password;
import com.example.social_network.factory.Factory;
import com.example.social_network.service.Service;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SignInController {

    private static final Factory factory = new Factory();
    public static Service service = factory.getService();
    public static String currentUser;

    @FXML
    Pane paneSignIn;

    @FXML
    Pane paneSignUp;

    @FXML
    TextField tfUsernameSignIn;

    @FXML
    PasswordField psPasswordSignIn;

    @FXML
    Button btnSignIn;

    @FXML
    Label warningMessageSignIn;

    @FXML
    TextField tfUsernameSignUp;

    @FXML
    PasswordField psPasswordSignUp;

    @FXML
    Label warningMessageSignUp;

    @FXML
    Circle btnClose;

    @FXML
    ImageView btnBack;

    public void signIn() throws IOException {
        warningMessageSignIn.setText("");

        if (tfUsernameSignIn.getText().isEmpty()) {
            warningMessageSignIn.setTextFill(Color.RED);
            warningMessageSignIn.setText("Username field cannot be empty!");
        } else {
            if (psPasswordSignIn.getText().isEmpty()) {
                warningMessageSignIn.setTextFill(Color.RED);
                warningMessageSignIn.setText("Password field cannot be empty!");
            } else {
                if (service.getUser(tfUsernameSignIn.getText()) != null && Objects.equals(service.getPassword(service.getUser(tfUsernameSignIn.getText()).getId()), new Password(service.getUser(tfUsernameSignIn.getText()).getId(), psPasswordSignIn.getText()))) {
                    currentUser = tfUsernameSignIn.getText();

                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/social_network/home.fxml")));
                    Stage stage = (Stage) (btnSignIn.getScene().getWindow());
                    Scene scene = new Scene(root);

                    Application.movableStage(root, stage);

                    stage.setScene(scene);
                    stage.show();
                } else {
                    warningMessageSignIn.setText("Incorrect username or password!");
                }
            }
        }
    }

    public void signUp() {
        if (tfUsernameSignUp.getText().isEmpty()) {
            warningMessageSignUp.setTextFill(Color.RED);
            warningMessageSignUp.setText("Username field cannot be empty!");
        } else {
            if (psPasswordSignUp.getText().isEmpty()) {
                warningMessageSignUp.setTextFill(Color.RED);
                warningMessageSignUp.setText("Password field cannot be empty!");
            } else {
                if (service.getUser(tfUsernameSignUp.getText()) != null) {
                    warningMessageSignUp.setTextFill(Color.RED);
                    warningMessageSignUp.setText("A user with this username already exists!");
                } else if (psPasswordSignUp.getText().length() < 6) {
                    warningMessageSignUp.setTextFill(Color.RED);
                    warningMessageSignUp.setText("Password must be longer than 6 characters!");
                } else if (psPasswordSignUp.getText().contains(" ")) {
                    warningMessageSignUp.setTextFill(Color.RED);
                    warningMessageSignUp.setText("Password must not contain spaces!");
                } else {
                    service.addUser(tfUsernameSignUp.getText());
                    service.addPassword(service.getUser(tfUsernameSignUp.getText()).getId(), psPasswordSignUp.getText());
                    warningMessageSignUp.setTextFill(Color.GREEN);
                    warningMessageSignUp.setText("Congratulation, your account has been \n successfully created.");
                }
            }
        }
    }

    public void changePane() {
        tfUsernameSignIn.setText("");
        psPasswordSignIn.setText("");

        tfUsernameSignUp.setText("");
        psPasswordSignUp.setText("");

        if (paneSignIn.isVisible()) {
            paneSignIn.setVisible(false);
            paneSignUp.setVisible(true);
        } else {
            paneSignIn.setVisible(true);
            paneSignUp.setVisible(false);
        }
    }

    public void signInOnEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            signIn();
        }
    }

    public void closeWindow() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}

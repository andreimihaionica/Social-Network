package com.example.social_network.controllers;

import com.example.social_network.domain.Message;
import com.example.social_network.service.PDFService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class StatisticsController {
    @FXML
    DatePicker startDate;

    @FXML
    DatePicker endDate;

    @FXML
    TextField fileName;

    @FXML
    TextField filePath;

    @FXML
    TextField searchUser;

    @FXML
    VBox vbox;

    @FXML
    Label warningMessage;

    public void verifyKey(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            showData();
        }
    }

    public void showData() {
        warningMessage.setText("");

        if (startDate.getValue() == null) {
            warningMessage.setText("Start Date can't be empty!");
        } else if (endDate.getValue() == null) {
            warningMessage.setText("End Date can't be empty!");
        } else if (searchUser.getText() == null) {
            warningMessage.setText("User can't be empty!");
        } else if (SignInController.service.getUser(searchUser.getText()) == null) {
            warningMessage.setText("User does not exist!");
        } else {
            String username = searchUser.getText();

            PDFService pdfService = new PDFService(SignInController.service);
            String string;

            string = pdfService.getNewFriends(getDate(startDate), getDate(endDate), SignInController.currentUser).size() + " new friends";
            vbox.getChildren().add(getHBox(string, true));
            vbox.getChildren().add(getHBox("", false));

            string = pdfService.noOfMessages(getDate(startDate), getDate(endDate), SignInController.currentUser) + " messages received";
            vbox.getChildren().add(getHBox(string, true));
            vbox.getChildren().add(getHBox("", false));

            string = "Messages from " + username;
            vbox.getChildren().add(getHBox(string, true));

            List<Message> messagesList = pdfService.getMessages(getDate(startDate), getDate(endDate), SignInController.currentUser, username);
            for (Message message : messagesList) {
                string = message.toString();
                vbox.getChildren().add(getHBox(string, false));
            }
        }
    }

    private HBox getHBox(String string, boolean circleOn) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(15);
        hBox.setPrefWidth(600);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setSpacing(10);

        Label label = new Label(string);
        label.setFont(new Font("Verdana ", 15));

        if (circleOn) {
            Circle circle = new Circle();
            circle.setRadius(3);
            circle.setStyle("-fx-fill: #05071F");

            hBox.getChildren().add(circle);
        }

        hBox.getChildren().add(label);

        return hBox;
    }

    public void generatePDF() {
        warningMessage.setText("");
        warningMessage.setTextFill(Color.RED);

        if (startDate.getValue() == null) {
            warningMessage.setText("Start Date can't be empty!");
        } else if (endDate.getValue() == null) {
            warningMessage.setText("End Date can't be empty!");
        } else if (searchUser.getText() == null) {
            warningMessage.setText("User can't be empty!");
        } else if (SignInController.service.getUser(searchUser.getText()) == null) {
            warningMessage.setText("User does not exist!");
        } else if (fileName.getText() == null) {
            warningMessage.setText("File Name can't be empty!");
        } else if (filePath.getText() == null) {
            warningMessage.setText("File Path can't be empty!");
        } else {
            String username = searchUser.getText();
            String file = filePath.getText() + "\\" + fileName.getText() + ".pdf";

            PDFService pdfService = new PDFService(SignInController.service);
            try {
                pdfService.statisticsPDF(getDate(startDate), getDate(endDate), SignInController.currentUser, username, file);
                warningMessage.setTextFill(Color.GREEN);
                warningMessage.setText("PDF file generate with success!");
            } catch (IOException exception) {
                warningMessage.setText("Incorrect File Path or File Name already exists!");
            }
        }
    }

    private Date getDate(DatePicker date) {
        LocalDate localDate;
        Instant instant;

        localDate = date.getValue();
        instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);
    }

}

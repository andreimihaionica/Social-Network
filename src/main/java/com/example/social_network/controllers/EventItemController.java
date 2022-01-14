package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class EventItemController {
    @FXML
    Label eventName;

    @FXML
    Label createdBy;

    @FXML
    Label lblLocation;

    @FXML
    Label lblDate;

    @FXML
    Label lblType;

    @FXML
    Label lblDescription;

    @FXML
    Button btnSubscribe;

    public void initialize(String name, String created_by, String location, String date, String type, String description, boolean subscribed) {
        eventName.setText(name);
        createdBy.setText("Created by " + created_by);
        lblLocation.setText(location);
        lblDate.setText(date);
        lblType.setText(type);
        lblDescription.setText("     " + description);
        if (subscribed) {
            btnSubscribe.setText("Unsubscribe");
            btnSubscribe.setOnAction(e -> {
                SignInController.service.unsubscribeFromEvent(name, SignInController.currentUser);
                btnSubscribe.setText("Unsubscribed");
                btnSubscribe.setDisable(true);
            });
        } else {
            btnSubscribe.setText("Subscribe");
            btnSubscribe.setOnAction(e -> {
                SignInController.service.subscribeToEvent(name, SignInController.currentUser);
                btnSubscribe.setText("Subscribed");
                btnSubscribe.setDisable(true);
            });
        }

    }
}

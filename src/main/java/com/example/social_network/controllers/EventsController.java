package com.example.social_network.controllers;

import  com.example.social_network.domain.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class EventsController {
    @FXML
    Pagination pagination;

    @FXML
    Pane pnlEvents;

    @FXML
    Pane pnlCreateNewEvent;

    @FXML
    TextField tfName;

    @FXML
    TextField tfLocation;

    @FXML
    TextField tfType;

    @FXML
    DatePicker date;

    @FXML
    TextArea lblDescription;

    @FXML
    Label warningMessage;

    @FXML
    Button btnCreateEvent;

    @FXML
    public void initialize() throws IOException {
        changePnlAllEvents();
    }

    public void changePnlAllEvents() {
        pagination.setPageCount(SignInController.service.noEvents());
        pagination.setPageFactory(this::createPage);

        pnlEvents.setVisible(true);
        pnlCreateNewEvent.setVisible(false);
    }

    public void changePnlCreateNewEvent() {
        tfName.setText("");
        tfLocation.setText("");
        tfType.setText("");
        lblDescription.setText("");
        warningMessage.setText("");
        btnCreateEvent.setDisable(false);
        date.setValue(LocalDate.now());

        pnlEvents.setVisible(false);
        pnlCreateNewEvent.setVisible(true);
    }

    public Node createPage(int pageIndex) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/eventItem.fxml"));
            Node node = fxmlLoader.load();
            EventItemController controller = fxmlLoader.getController();
            Set<Event> eventSet = SignInController.service.getEventsOnPage(pageIndex);

            for (Event event : eventSet) {
                controller.initialize(event.getName(), event.getCreatedBy().getUsername(), event.getLocation(), event.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyy")), event.getType(), event.getDescription(), event.getSubscribers().contains(SignInController.service.getUser(SignInController.currentUser)));
            }

            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createEvent() {
        warningMessage.setText("");
        warningMessage.setTextFill(Color.RED);

        if(tfName.getText() == null) {
            warningMessage.setText("Event Name can't be empty!");
        } else if(tfLocation.getText() == null) {
            warningMessage.setText("Location can't be empty!");
        } else if(tfType.getText() == null) {
            warningMessage.setText("Event Type can't be empty!");
        } else if(date.getValue() == null) {
            warningMessage.setText("Date can't be empty!");
        } else if(lblDescription.getText() == null) {
            warningMessage.setText("Description can't be empty!");
        } else {


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime = LocalDateTime.parse(date.getValue().toString() + " 00:00", formatter);

            SignInController.service.createEvent(SignInController.currentUser,
                    tfName.getText(),
                    lblDescription.getText(),
                    tfLocation.getText(),
                    tfType.getText(),
                    dateTime);

            btnCreateEvent.setDisable(true);

            warningMessage.setText("Event created!");
            warningMessage.setTextFill(Color.GREEN);
        }
    }

    public void refresh() throws IOException {
        initialize();
    }

}
package com.example.social_network.controllers;

import com.example.social_network.domain.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Set;


public class AllEventsController {
    @FXML
    Pagination pagination;

    @FXML
    public void initialize() {
        pagination.setPageCount(SignInController.service.noEvents());
        pagination.setPageFactory(this::createPage);
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

    public void refresh() {
        initialize();
    }

}

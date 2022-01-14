package com.example.social_network.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Pagination;

import java.io.IOException;


public class AllEventsController {
    @FXML
    Pagination pagination;

    @FXML
    public void initialize() {
        pagination.setPageFactory(this::createPage);
    }

    public Node createPage(int pageIndex) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/social_network/eventItem.fxml"));
            Node node = fxmlLoader.load();

            return node;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

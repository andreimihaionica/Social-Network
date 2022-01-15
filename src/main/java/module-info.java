module com.example.social_network {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires pdfbox.app;

    opens com.example.social_network to javafx.fxml;
    exports com.example.social_network;
    opens com.example.social_network.util to javafx.graphics, javafx.fxml, javafx.base;
    exports com.example.social_network.service to javafx.graphics, javafx.fxml, javafx.base;
    exports com.example.social_network.util to javafx.graphics, javafx.fxml, javafx.base;
    exports com.example.social_network.domain to javafx.graphics, javafx.fxml, javafx.base;
    exports com.example.social_network.controllers;
    opens com.example.social_network.controllers to javafx.fxml;
}
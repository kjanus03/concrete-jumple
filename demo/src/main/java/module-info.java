module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    requires com.almasb.fxgl.all;
    requires org.mongodb.driver.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.media;

//    opens com.example.platformer to javafx.fxml, com.fasterxml.jackson.databind;
//    exports com.example.platformer;
    exports com.example.platformer.generators;
    opens com.example.platformer.generators to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.platformer.entities;
    opens com.example.platformer.entities to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.platformer.map;
    opens com.example.platformer.map to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.platformer.core;
    opens com.example.platformer.core to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.platformer.ui;
    opens com.example.platformer.ui to com.fasterxml.jackson.databind, javafx.fxml;
}
module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    requires com.almasb.fxgl.all;
    requires org.mongodb.driver.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.media;

    opens com.example.platformer to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.platformer;
    exports Generators;
    opens Generators to com.fasterxml.jackson.databind, javafx.fxml;
}
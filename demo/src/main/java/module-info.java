module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;

    requires com.almasb.fxgl.all;
    requires org.mongodb.driver.core;
    requires com.fasterxml.jackson.databind;

    opens com.example.platformer to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.platformer;
}
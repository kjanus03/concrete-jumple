package com.example.platformer.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EndScreen {
    private double elapsedTime;
    private String username;

    // Constructor with elapsed time
    public EndScreen(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Scene createEndScreenScene() {
        // Create the title text and elapsed time
        Text title = new Text("You win!");
        title.setFill(Color.WHITE);
        title.getStyleClass().add("end-screen-title");

        Text timeText = new Text("Elapsed Time: " + String.format("%.2f", elapsedTime) + " seconds");
        timeText.setFill(Color.WHITE);
        timeText.getStyleClass().add("end-screen-text");

        // Create a text field for the player to enter their username
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.getStyleClass().add("end-screen-textfield");

        // Create the submit button
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("end-screen-button");
        submitButton.setOnAction(event -> {
            // Store the username entered by the player
            this.username = usernameField.getText();
            if (this.username == null || this.username.trim().isEmpty()) {
                this.username = "Player"; // Default to "Player" if nothing is entered
            }

            // You can also close the window here if you wish
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.close(); // Close the EndScreen window

            // Pass the username back to the listener or callback
            onSubmit();
        });

        // Layout
        VBox layout = new VBox(10, title, timeText, usernameField, submitButton);
        layout.getStyleClass().add("end-screen-layout");

        // Create the scene and apply the stylesheet
        Scene scene = new Scene(layout, 400, 300);
        String stylesheetUrl = getClass().getResource("/css/endscreen.css").toExternalForm();
        scene.getStylesheets().add(stylesheetUrl);

        return scene;
    }

    // Method to return the username
    public String getUsername() {
        return this.username;
    }

    // Method called when the submit button is clicked
    private void onSubmit() {
        // Pass the username and time to the game controller or relevant handler
        if (username != null && !username.trim().isEmpty()) {
            System.out.println("Username: " + username); // Log the username if necessary
        }
    }
}

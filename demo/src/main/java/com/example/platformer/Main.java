package com.example.platformer;

import com.example.platformer.core.UserSettings;
import com.example.platformer.ui.MenuScreen;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load user settings
        UserSettings userSettings = new UserSettings();

        // Create a root pane for your UI elements
        StackPane root = new StackPane();
        Scene scene = new Scene(root, userSettings.getWidth(), userSettings.getHeight());

        // Initialize the MenuScreen and pass the Stage to it
        MenuScreen menuScreen = new MenuScreen(userSettings);
        menuScreen.setRoot(root, primaryStage);  // Pass the root and the primary stage to the MenuScreen

        // Apply any stylesheets (if you have them)
        scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        // Configure the primary stage
        primaryStage.setTitle("Concrete Jumple - Platformer Game");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(userSettings.isFullscreen());
        if (!userSettings.isFullscreen()) {
            primaryStage.setMaximized(true);
        }
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

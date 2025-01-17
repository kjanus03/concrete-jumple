package com.example.platformer.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Pos;

public class MenuScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the root layout
        StackPane root = new StackPane();

        int screenWidth = 1920;
        int screenHeight = 1080;

        // Add a semi-transparent black rectangle for the darkening effect
        Rectangle darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));  // Black with 70% opacity

        // Bind the rectangle to the root pane's size to fill the entire screen
        darkOverlay.widthProperty().bind(root.widthProperty());
        darkOverlay.heightProperty().bind(root.heightProperty());

        // Create a title label for the game
        Label gameTitle = new Label("CONCRETE JUMPLE");
        gameTitle.getStyleClass().add("game-title");  // Style class for CSS
        gameTitle.setTranslateY(-200);  // Adjust position as needed

        // Create buttons for the menu
        Button startButton = new Button("Start Game");
        Button highScoresButton = new Button("High Scores");
        Button exitButton = new Button("Exit");

        // Add event handlers for the buttons
        startButton.setOnAction(event -> {
            System.out.println("Start Game pressed");
            Pane gamePane = new Pane();
            Scene gameScene = new Scene(gamePane, screenWidth, screenHeight);
            GameController gameController = new GameController(gamePane, gameScene);
            gameController.startGame();
        });

        highScoresButton.setOnAction(event -> {
            System.out.println("High Scores pressed");
            showHighScores();
        });

        exitButton.setOnAction(event -> {
            System.out.println("Exit pressed");
            primaryStage.close();
        });

        // Style the buttons
        startButton.getStyleClass().add("menu-button");
        highScoresButton.getStyleClass().add("menu-button");
        exitButton.getStyleClass().add("menu-button");

        // Arrange buttons in a vertical layout
        VBox menuLayout = new VBox(20, startButton, highScoresButton, exitButton);
        menuLayout.setTranslateY(100);
        menuLayout.getStyleClass().add("menu-layout");

        // Add the components to the root
        root.getChildren().addAll(darkOverlay, gameTitle, menuLayout);

        // Create the scene and apply the CSS
        Scene scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        // Set up the primary stage
        primaryStage.setTitle("Platformer Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showHighScores() {
        // Logic for displaying high scores can go here
        System.out.println("Showing high scores...");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

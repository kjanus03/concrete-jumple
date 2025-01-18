package com.example.platformer.ui;

import com.example.platformer.highscores.DatabaseManager;
import com.example.platformer.highscores.HighScore;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.geometry.Pos;

import java.util.Comparator;
import java.util.List;

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
            // start game code here
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
        DatabaseManager databaseManager = new DatabaseManager();
        Stage highScoresStage = new Stage();
        highScoresStage.setTitle("High Scores");

        List<HighScore> highScores = databaseManager.getHighScores();
        highScores.sort(Comparator.comparingDouble(HighScore::getScore)); // Sort by score (lowest to highest)

        // Create a TableView for the high scores
        TableView<HighScore> highScoresTable = new TableView<>();

        // Create columns for player's name, score, and date
        TableColumn<HighScore, String> nameColumn = new TableColumn<>("Player");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setMinWidth(150);

        TableColumn<HighScore, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setMinWidth(100);

        TableColumn<HighScore, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); // Assuming HighScore has a `date` property
        dateColumn.setMinWidth(150);

        // Add columns to the table
        highScoresTable.getColumns().addAll(nameColumn, scoreColumn, dateColumn);
        highScoresTable.setItems(FXCollections.observableArrayList(highScores.subList(0, Math.min(10, highScores.size()))));
        highScoresTable.getStyleClass().add("high-scores-table");

        // Create a VBox layout
        VBox highScoresLayout = new VBox(20);
        highScoresLayout.setAlignment(Pos.CENTER);

        // Add a title
        Label title = new Label("High Scores");
        title.getStyleClass().add("high-score-title");

        // Add title and table to the layout
        highScoresLayout.getChildren().addAll(title, highScoresTable);

        // Create a scene and apply the CSS
        Scene highScoresScene = new Scene(highScoresLayout, 500, 600);
        Rectangle darkOverlay = new Rectangle(500, 600);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));  // Black with 70% opacity
        highScoresScene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());
        highScoresStage.setScene(highScoresScene);
        highScoresStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

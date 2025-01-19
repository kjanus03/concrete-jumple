package com.example.platformer.ui;

import com.example.platformer.core.UserSettings;
import com.example.platformer.highscores.DatabaseManager;
import com.example.platformer.highscores.HighScore;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;

public class HighScoresScreen {

    private final StackPane root;
    private final UserSettings userSettings;
    private final Stage stage;

    public HighScoresScreen(StackPane root, UserSettings userSettings, Stage stage) {
        this.root = root;
        this.userSettings = userSettings;
        this.stage = stage;
    }

    public void setRoot() {
        root.getChildren().clear();  // Clear current root contents

        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();
        DatabaseManager databaseManager = new DatabaseManager();

        // Get all high scores from the database
        List<HighScore> highScores = databaseManager.getHighScores();

        // Sort by score (ascending, so the quickest times come first)
        highScores.sort(Comparator.comparingDouble(HighScore::getScore));

        // Get only the top 10 quickest times (lowest scores)
        List<HighScore> top10HighScores = highScores.subList(0, Math.min(10, highScores.size()));

        // Create a TableView for the high scores
        TableView<HighScore> highScoresTable = new TableView<>();
        double tableWidth = screenWidth * 0.8;

        highScoresTable.setMaxWidth(tableWidth);

        // Create columns for player's name, score (time), and date
        TableColumn<HighScore, String> nameColumn = new TableColumn<>("Player");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setPrefWidth(tableWidth/4);

        TableColumn<HighScore, Double> scoreColumn = new TableColumn<>("Time (Score)");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setPrefWidth(tableWidth/4);

        TableColumn<HighScore, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setPrefWidth(tableWidth/2-10);

        nameColumn.setResizable(false);
        scoreColumn.setResizable(false);
        dateColumn.setResizable(false);

        // Add columns to the table
        highScoresTable.getColumns().addAll(nameColumn, scoreColumn, dateColumn);

        // Set the top 10 high scores in the table
        highScoresTable.setItems(FXCollections.observableArrayList(top10HighScores));

        // Apply CSS class to the table
        highScoresTable.getStyleClass().add("high-scores-table");

        // Create a VBox layout
        VBox highScoresLayout = new VBox(20);
        highScoresLayout.setAlignment(Pos.CENTER);

        // Add a title
        Label title = new Label("High Scores");
        title.getStyleClass().add("high-score-title");  // Applying the CSS class for title

        // Back button to return to the main menu
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("menu-button");  // Applying the CSS class for buttons
        backButton.setOnAction(event -> {
            // Return to MenuScreen
            MenuScreen menuScreen = new MenuScreen(userSettings);
            menuScreen.setRoot(root, this.stage);  // Use the class-level stage
        });
        // Add title, table, and back button to the layout
        highScoresLayout.getChildren().addAll(title, highScoresTable, backButton);

        // Create a dark overlay rectangle (similar to MenuScreen)
        Rectangle darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.7));  // Black with 80% opacity

        // Stack the dark rectangle and the content
        StackPane highScoresRoot = new StackPane();
        highScoresRoot.getChildren().addAll(darkOverlay, highScoresLayout);

        // Set the highScoresRoot to the current root of the scene
        root.getChildren().add(highScoresRoot);
    }
}

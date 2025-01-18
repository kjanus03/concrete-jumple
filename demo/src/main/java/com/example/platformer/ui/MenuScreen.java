package com.example.platformer.ui;

import com.example.platformer.core.GameStarter;
import com.example.platformer.highscores.DatabaseManager;
import com.example.platformer.highscores.HighScore;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
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

import java.util.Comparator;
import java.util.List;

public class MenuScreen extends Application {

    private GameStarter gameStarter;

    public MenuScreen() {
        this.gameStarter = new GameStarter();
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        int screenWidth = 1920;
        int screenHeight = 1080;

        // Dark overlay for the menu background
        Rectangle darkOverlay = createDarkOverlay(screenWidth, screenHeight);


        // Title label and buttons
        VBox menuLayout = createMenuLayout();

        root.getChildren().addAll(darkOverlay, menuLayout);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        primaryStage.setTitle("Platformer Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Rectangle createDarkOverlay(int screenWidth, int screenHeight) {
        Rectangle darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));  // Black with 80% opacity
        return darkOverlay;
    }

    private VBox createMenuLayout() {
        Label gameTitle = new Label("CONCRETE JUMPLE");
        gameTitle.getStyleClass().add("game-title");  // Style class for CSS
        gameTitle.setTranslateY(-100);  // Adjust position as needed


        Button startButton = createMenuButton("Start Game", event -> gameStarter.startGame());
        Button highScoresButton = createMenuButton("High Scores", event -> showHighScores());
        Button exitButton = createMenuButton("Exit", event -> System.exit(0));

        VBox menuLayout = new VBox(20, gameTitle, startButton, highScoresButton, exitButton);
//        menuLayout.setTranslateY(100);
        menuLayout.getStyleClass().add("menu-layout");
        return menuLayout;
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.getStyleClass().add("menu-button");
        return button;
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
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date")); // Assuming HighScore has a date property
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

    public void show(Stage primaryStage) {
        start(primaryStage);
    }

}

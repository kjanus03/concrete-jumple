package com.example.platformer.ui;

import com.example.platformer.core.GameStarter;
import com.example.platformer.ui.MusicPlayer;
import com.example.platformer.core.UserSettings;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MenuScreen {

    private final UserSettings userSettings;
    private Stage primaryStage;

    public MenuScreen(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public void setRoot(StackPane root, Stage stage) {
        this.primaryStage = stage; // Store the reference to the primary stage

        root.getChildren().clear();  // Clear the current root contents

        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();

        // Dark overlay for the menu background
        Rectangle darkOverlay = createDarkOverlay(screenWidth, screenHeight);

        // Title label and buttons
        VBox menuLayout = createMenuLayout(root, stage);

        root.getChildren().addAll(darkOverlay, menuLayout);
    }

    private VBox createMenuLayout(StackPane root, Stage stage) {
        Label gameTitle = new Label("CONCRETE JUMPLE");
        gameTitle.getStyleClass().add("game-title");

        Button startButton = createMenuButton("Start Game", event -> {
            startGame(stage);  // Call the method to start the game
        });

        Button highScoresButton = createMenuButton("High Scores", event -> {
            HighScoresScreen highScoresScreen = new HighScoresScreen(root, userSettings, stage);  // Pass stage
            highScoresScreen.setRoot();  // Switch to HighScoresScreen
        });

        Button settingsButton = createMenuButton("Settings", event -> {
            SettingsScreen settingsScreen = new SettingsScreen(root, stage, userSettings, new MusicPlayer());
            settingsScreen.setRoot();  // Switch to SettingsScreen with the current stage
        });

        Button exitButton = createMenuButton("Exit", event -> System.exit(0));

        VBox menuLayout = new VBox(20, gameTitle, startButton, highScoresButton, settingsButton, exitButton);
        menuLayout.getStyleClass().add("menu-layout");
        return menuLayout;
    }

    private Rectangle createDarkOverlay(int screenWidth, int screenHeight) {
        Rectangle darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));  // Black with 80% opacity
        return darkOverlay;
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.getStyleClass().add("menu-button");
        return button;
    }

    private void startGame(Stage stage) {
        // Initialize MusicPlayer for the game
        MusicPlayer gameMusicPlayer = new MusicPlayer();
        gameMusicPlayer.setGameMusic("src/main/resources/audio/gameMusic.mp3");  // Example game music

        // Stop the menu music
        gameMusicPlayer.stop();

        // Create GameStarter to launch the game
        GameStarter gameStarter = new GameStarter(gameMusicPlayer, userSettings, primaryStage);

        // Hide the MenuScreen stage and start the game
        primaryStage.hide(); // Hide the menu screen
        gameStarter.startGame(); // Pass the stage to GameStarter to manage the transition
    }
}

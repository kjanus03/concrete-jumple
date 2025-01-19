package com.example.platformer.core;

import com.example.platformer.highscores.DatabaseManager;
import com.example.platformer.highscores.HighScore;
import com.example.platformer.ui.BuffSidebar;
import com.example.platformer.ui.MusicPlayer;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameStarter implements GameEndListener {

    private MusicPlayer gamePlayer;
    private MusicPlayer menuPlayer;
    private DatabaseManager databaseManager;
    private Scene scene;
    private Stage primaryStage;
    private UserSettings userSettings;

    public GameStarter(MusicPlayer menuPlayer, UserSettings userSettings, Stage primaryStage) {
        this.menuPlayer = menuPlayer;
        this.gamePlayer = new MusicPlayer();
        this.gamePlayer.setGameMusic("src/main/resources/audio/soundtrack.mp3");
        this.databaseManager = new DatabaseManager();
        this.userSettings = userSettings;
        this.primaryStage = primaryStage;
    }

    public void startGame() {
        UserSettings userSettings = new UserSettings();
        int scalingFactor = userSettings.getScalingFactor();

        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();
        int sidebarWidth = (int) screenHeight/4;

        // Root layout with a BorderPane
        BorderPane root = new BorderPane();

        // Gameplay Pane
        Pane gamePane = new Pane();

        // Add background image
        Image backgroundImage = new Image(getClass().getResource("/sprites/background/Steampunk/darkblue1.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(screenWidth);
        backgroundView.setFitHeight(screenHeight);
        backgroundView.setPreserveRatio(false);
        gamePane.getChildren().add(backgroundView);

        root.setCenter(gamePane);

        // Sidebar
        BuffSidebar buffSidebar = new BuffSidebar(sidebarWidth);
        root.setRight(buffSidebar.getSidebar());

        // Scene
        this.scene = new Scene(root, screenWidth, screenHeight);

        // GameController
        GameController gameController = new GameController(gamePane, scene, backgroundView, buffSidebar, scalingFactor);
        gameController.startGame();
        gameController.setGameEndListener(this);

        // Music and Database
        gamePlayer.playGame();

        Stage stage = new Stage();
        stage.setTitle("ConcreteJumple");
        stage.setFullScreen(userSettings.isFullscreen());
        if (!userSettings.isFullscreen()) {
            stage.setMaximized(true);
        }
        stage.setResizable(false);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onGameEnd(HighScore score) {
        // Handle game end logic, such as stopping music and closing the stage
        if (gamePlayer != null) {
            gamePlayer.stop();
            gamePlayer = null;
        }
        this.menuPlayer.playMenu();

        // Save the score to the database
        databaseManager.insertHighScore(score);
        System.out.println("Score saved to the database: " + score);

        // Close the current game stage
        Stage currentStage = (Stage) ((Pane) scene.getRoot()).getScene().getWindow();
        currentStage.close();

        // Re-show the MenuScreen stage
        primaryStage.show();
        primaryStage.setFullScreen(userSettings.isFullscreen());
        if (!userSettings.isFullscreen()) {
            primaryStage.setMaximized(true);
        }
    }
}

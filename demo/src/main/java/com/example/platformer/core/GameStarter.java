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

import java.util.Objects;

public class GameStarter implements GameEndListener, GameAbortListener {
    private static final String GAME_MUSIC_PATH = "src/main/resources/audio/soundtrack.mp3";
    private static final double SIDEBAR_WIDTH_RATIO = 0.25;
    private static final String BACKGROUND_IMAGE_PATH = "/sprites/background/Steampunk/darkblue1.png";
    private static final String GAME_TITLE = "ConcreteJumple";


    private MusicPlayer musicPlayer;
    private Scene scene;
    private final MusicPlayer menuPlayer;
    private final DatabaseManager databaseManager;
    private final Stage primaryStage;
    private final UserSettings userSettings;

    public GameStarter(MusicPlayer menuPlayer, UserSettings userSettings, Stage primaryStage) {
        this.menuPlayer = menuPlayer;
        this.musicPlayer = new MusicPlayer();
        this.musicPlayer.setGameMusic(GAME_MUSIC_PATH);
        this.databaseManager = new DatabaseManager();
        this.userSettings = userSettings;
        this.primaryStage = primaryStage;
    }

    public void startGame() {
        UserSettings userSettings = new UserSettings();
        int scalingFactor = userSettings.getScalingFactor();

        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();
        int sidebarWidth = (int) (screenWidth * SIDEBAR_WIDTH_RATIO);

        BorderPane root = new BorderPane();

        // The Gameplay Pane
        Pane gamePane = new Pane();

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource(BACKGROUND_IMAGE_PATH)).toExternalForm());
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
        gameController.setGameAbortListener(this, primaryStage, userSettings.isFullscreen());

        // Music and Database
        musicPlayer.setVolume(userSettings.getVolume());
        musicPlayer.play();

        Stage stage = new Stage();
        stage.setTitle(GAME_TITLE);
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
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
        this.menuPlayer.play();
        databaseManager.insertHighScore(score);

        // Close the current game stage
        Stage currentStage = (Stage) scene.getRoot().getScene().getWindow();
        currentStage.close();

        // Re-show the MenuScreen stage
        primaryStage.show();
        primaryStage.setFullScreen(userSettings.isFullscreen());
        if (!userSettings.isFullscreen()) {
            primaryStage.setMaximized(true);
        }
    }

    @Override
    public void onGameAbort() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
        this.menuPlayer.play();
    }
}

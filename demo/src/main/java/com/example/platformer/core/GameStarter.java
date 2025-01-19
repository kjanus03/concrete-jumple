package com.example.platformer.core;

import com.example.platformer.ui.BuffSidebar;
import com.example.platformer.ui.MusicPlayer;
import com.example.platformer.highscores.DatabaseManager;
import com.example.platformer.highscores.HighScore;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameStarter implements GameEndListener {

    private MusicPlayer gamePlayer;
    private MusicPlayer menuPlayer;
    private DatabaseManager databaseManager;
    private Scene scene;

    public GameStarter(MusicPlayer menuPlayer) {
        this.menuPlayer = menuPlayer;
        this.gamePlayer = new MusicPlayer();
        this.gamePlayer.setGameMusic("src/main/resources/audio/soundtrack.mp3");
        this.databaseManager = new DatabaseManager();
    }

    public void startGame() {

        // hardcoded resolution
        int screenWidth = 1280;
        int screenHeight = 720;
        int sidebarWidth = 180;

        // Root layout with a BorderPane
        BorderPane root = new BorderPane();

        // Gameplay Pane
        Pane gamePane = new Pane();

        // Add background image
        Image backgroundImage = new Image(getClass().getResource("/sprites/background/Steampunk/darkblue1.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(screenWidth); // Set width to match your scene or window
        backgroundView.setFitHeight(screenHeight); // Set height to match your scene or window
        backgroundView.setPreserveRatio(false);
        gamePane.getChildren().add(backgroundView);

        root.setCenter(gamePane);

        // Sidebar
        BuffSidebar buffSidebar = new BuffSidebar(sidebarWidth);
        root.setRight(buffSidebar.getSidebar());

        // Scene
        this.scene = new Scene(root, screenWidth, screenHeight); // Adjusted width for the sidebar

        // GameController
        GameController gameController = new GameController(gamePane, scene, backgroundView, buffSidebar);
        gameController.startGame();
        gameController.setGameEndListener(this);

        // Music and Database
        gamePlayer.playGame();

        Stage stage = new Stage();
        stage.setTitle("Platformer Game");
        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void onGameEnd(HighScore score) {
        // Stop the background music
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

    }


}

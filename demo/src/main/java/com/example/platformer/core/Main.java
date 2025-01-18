package com.example.platformer.core;

import com.example.platformer.ui.BuffSidebar;
import com.example.platformer.ui.MusicPlayer;
import com.example.platformer.highscores.DatabaseManager;
import com.example.platformer.highscores.HighScore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application implements GameEndListener {

    private MusicPlayer musicPlayer;
    private DatabaseManager databaseManager;
    private BuffSidebar buffSidebar;

    @Override
    public void start(Stage stage) {
        // Root layout with a BorderPane
        BorderPane root = new BorderPane();

        // Gameplay Pane
        Pane gamePane = new Pane();

        // Add background image
        Image backgroundImage = new Image(getClass().getResource("/sprites/background/Steampunk/darkblue1.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(800); // Set width to match your scene or window
        backgroundView.setFitHeight(600); // Set height to match your scene or window
        backgroundView.setPreserveRatio(false); // Stretch to fill, or true to maintain aspect ratio
        gamePane.getChildren().add(backgroundView);

        root.setCenter(gamePane);

        // Sidebar
        buffSidebar = new BuffSidebar();
        root.setRight(buffSidebar.getSidebar());

        // Scene
        Scene scene = new Scene(root, 940, 600); // Adjusted width for the sidebar

        // GameController
        GameController gameController = new GameController(gamePane, scene, backgroundView);
        gameController.setBuffSidebar(buffSidebar); // Pass the sidebar to GameController
        gameController.setGameEndListener(this);
        gameController.startGame();

        // Music and Database
        this.musicPlayer = new MusicPlayer("src/main/resources/audio/soundtrack.mp3");
        musicPlayer.play();
        this.databaseManager = new DatabaseManager();

        stage.setTitle("Platformer Game");
        stage.setScene(scene);
//        stage.setFullScreen(true);
//        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void onGameEnd(HighScore score) {
        musicPlayer = null;
        databaseManager.insertHighScore(score);
        System.out.println("Score saved to the database: " + score);
    }

         public static void main(String[] args) {
        launch(args);
    }
}

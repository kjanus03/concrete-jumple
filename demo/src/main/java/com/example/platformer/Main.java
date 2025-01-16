package com.example.platformer;

import highscores.DatabaseManager;
import highscores.HighScore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application implements GameEndListener{

    private MusicPlayer musicPlayer;
    private DatabaseManager databaseManager;


    @Override
    public void start(Stage stage) {
//        Config config = new Config();
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        GameController gameController = new GameController(root, scene);
        gameController.startGame();
        gameController.setGameEndListener(this);

        this.musicPlayer = new MusicPlayer("src/main/assets/audio/soundtrack.mp3");
        musicPlayer.play();

        this.databaseManager = new DatabaseManager();

        stage.setTitle("Platformer Game");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onGameEnd(HighScore score) {
        this.musicPlayer = null;
        databaseManager.insertHighScore(score);
        System.out.println("Score saved to the database: " + score);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

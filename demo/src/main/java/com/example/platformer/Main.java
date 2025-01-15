package com.example.platformer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    private MusicPlayer musicPlayer;


    @Override
    public void start(Stage stage) {
//        Config config = new Config();
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        GameController gameController = new GameController(root, scene);
        gameController.startGame();

        this.musicPlayer = new MusicPlayer("src/main/assets/audio/soundtrack.mp3");
        musicPlayer.play();

        stage.setTitle("Platformer Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

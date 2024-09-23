package com.example.platformer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();  // The root pane where all game elements will be added
        Scene scene = new Scene(root, 800, 600);  // Window size: 800x600

        // Initialize GameController, which manages the game
        GameController gameController = new GameController(root);
        gameController.startGame(scene);  // Pass the scene to the controller for key listeners

        stage.setTitle("Platformer Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

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

import java.awt.Dimension;
import java.awt.Toolkit;

public class GameStarter {

    private MusicPlayer musicPlayer;
    private DatabaseManager databaseManager;

    public GameStarter() {
        this.musicPlayer = new MusicPlayer("src/main/resources/audio/soundtrack.mp3");
        this.databaseManager = new DatabaseManager();
    }

    public void startGame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();
        int sidebarWidth = 140;

        // Root layout with a BorderPane
        BorderPane root = new BorderPane();

        // Gameplay Pane
        Pane gamePane = new Pane();

        // Add background image
        Image backgroundImage = new Image(getClass().getResource("/sprites/background/Steampunk/darkblue1.png").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(screenWidth - sidebarWidth); // Set width to match your scene or window
        backgroundView.setFitHeight(screenHeight); // Set height to match your scene or window
        backgroundView.setPreserveRatio(false); // Stretch to fill, or true to maintain aspect ratio
        gamePane.getChildren().add(backgroundView);

        root.setCenter(gamePane);

        // Sidebar
        BuffSidebar buffSidebar = new BuffSidebar(sidebarWidth);
        root.setRight(buffSidebar.getSidebar());

        // Scene
        Scene scene = new Scene(root, screenWidth, screenHeight); // Adjusted width for the sidebar

        // GameController
        GameController gameController = new GameController(gamePane, scene, backgroundView, buffSidebar);
        gameController.startGame();

        // Music and Database
        musicPlayer.play();

        Stage stage = new Stage();
        stage.setTitle("Platformer Game");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
}

package com.example.platformer.ui;

import com.example.platformer.core.GameStarter;
import com.example.platformer.core.UserSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class MenuScreen extends Application {

    private GameStarter gameStarter;
    private UserSettings userSettings;
    private MusicPlayer menuPlayer;

    public MenuScreen(UserSettings userSettings, MusicPlayer menuPlayer) {
        this.menuPlayer = menuPlayer;
        this.userSettings = userSettings;
    }

    public MenuScreen(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();

        // Dark overlay for the menu background
        Rectangle darkOverlay = createDarkOverlay(screenWidth, screenHeight);

        // Title label and buttons
        VBox menuLayout = createMenuLayout(primaryStage);

        root.getChildren().addAll(darkOverlay, menuLayout);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        scene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        if (menuPlayer == null) {
            this.menuPlayer = new MusicPlayer();
            this.menuPlayer.setMenuMusic("src/main/resources/audio/menu_music.mp3");
            this.menuPlayer.playMenu();
        }


        primaryStage.setTitle("Platformer Game");
        primaryStage.setScene(scene);
//        userSettings.applyFullScreen(primaryStage);
        primaryStage.show();
    }

    private Rectangle createDarkOverlay(int screenWidth, int screenHeight) {
        Rectangle darkOverlay = new Rectangle(1920, 1080);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));  // Black with 80% opacity
        return darkOverlay;
    }

    private VBox createMenuLayout(Stage primaryStage) {
        Label gameTitle = new Label("CONCRETE JUMPLE");
        gameTitle.getStyleClass().add("game-title");  // Style class for CSS
//        gameTitle.setTranslateY(-100);  // Adjust position as needed

        Button startButton = createMenuButton("Start Game", event -> {

            this.menuPlayer.stop();

            System.out.println("Stopping menu music");
            this.gameStarter = new GameStarter(menuPlayer);
            this.gameStarter.startGame();
        });

        Button highScoresButton = createMenuButton("High Scores", event -> {
            HighScoresScreen highScoresScreen = new HighScoresScreen(primaryStage, primaryStage.getScene(), userSettings);
            highScoresScreen.show();
        });

        Button settingsButton = createMenuButton("Settings", event -> {
            SettingsScreen settingsScreen = new SettingsScreen(primaryStage, primaryStage.getScene(), userSettings, menuPlayer);
            settingsScreen.show();
        });

        Button exitButton = createMenuButton("Exit", event -> System.exit(0));

        VBox menuLayout = new VBox(20, gameTitle, startButton, highScoresButton, settingsButton, exitButton);
        menuLayout.getStyleClass().add("menu-layout");
        return menuLayout;
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.getStyleClass().add("menu-button");
        return button;
    }

    public void show(Stage primaryStage) {
        start(primaryStage);
    }
}

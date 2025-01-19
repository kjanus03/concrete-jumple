package com.example.platformer.ui;

import com.example.platformer.core.GameAbortListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class PauseScreen extends StackPane {
    private final Text timerText;
    private final Button restartButton;
    private final Button menuButton;
    private final GameAbortListener gameAbortListener;

    public PauseScreen(double width, double height, Scene scene, Stage primaryStage, boolean isFullscreen, GameAbortListener listener) {
        // Set up the pause screen
        setPrefSize(width, height);
        this.gameAbortListener = listener;

        // Semi-transparent background
        Rectangle overlay = new Rectangle(width, height);
        overlay.setFill(Color.BLACK);
        overlay.setOpacity(0.6);
        this.getStylesheets().add(getClass().getResource("/css/pause.css").toExternalForm());

        // Pause message
        Text pauseText = new Text("The game is paused.");
        pauseText.setFont(Font.font("Press Start 2P", 40));
        pauseText.setFill(Color.WHITE);


        Text unpauseText = new Text("Click ESC to unpause.");
        unpauseText.setFont(Font.font("Press Start 2P", 20));
        unpauseText.setFill(Color.WHITE);

        // Timer text
        timerText = new Text();
        timerText.setFont(Font.font("Press Start 2P", 20));
        timerText.setFill(Color.WHITE);

        // Restart button
        restartButton = new Button("Restart Game");
        restartButton.setFont(Font.font("Press Start 2P", 20));
        restartButton.getStyleClass().add("pause-button");

        menuButton = new Button("Go To Menu");
        menuButton.setFont(Font.font("Press Start 2P", 20));
        menuButton.getStyleClass().add("pause-button");
        menuButton.setOnAction(e -> {
            this.gameAbortListener.onGameAbort();
            // Close the current game stage
            Stage currentStage = (Stage) ((Pane) scene.getRoot()).getScene().getWindow();
            currentStage.close();

            // Re-show the MenuScreen stage
            primaryStage.show();
            primaryStage.setFullScreen(isFullscreen);
            if (!isFullscreen) {
                primaryStage.setMaximized(true);
            }
        });

        // Layout container
        VBox layout = new VBox(20, pauseText, unpauseText, timerText, restartButton, menuButton);
        layout.setAlignment(Pos.CENTER);

        // Add elements to the stack pane
        getChildren().addAll(overlay, layout);

        // Initially hidden
        setVisible(false);
    }

    /**
     * Updates the timer text displayed on the pause screen.
     *
     * @param elapsedTime the elapsed time to display
     */
    public void updateTimer(double elapsedTime) {
        timerText.setText("Current time: " + String.format("%.2f seconds", elapsedTime));
    }

    public Button getRestartButton() {
        return restartButton;
    }
}

package com.example.platformer;

import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class GameTimer {
    private double elapsedTime = 0;  // Elapsed time in seconds
    private Text timerText;  // Text node to display the timer

    public GameTimer() {
        // Create a Text node to display the timer
        timerText = new Text();
        timerText.setFont(Font.font(20));  // Set font size for the timer text
        timerText.setFill(Color.BLACK);  // Set text color to white
        timerText.setX(680);  // Position the timer text near the top-right corner
        timerText.setY(30);
    }

    // Method to update the timer
    public void update(double deltaTime, double timerY) {
        if (elapsedTime < 0) return;  // Stop if the game is finished
        elapsedTime += deltaTime;  // Increment elapsed time
        timerText.setY(timerY);  // Update the Y position of the timer text
        displayTime();  // Update the displayed time
    }

    // Method to update the text to show elapsed time in the format "mm:ss"
    private void displayTime() {
        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        int milliseconds = (int) ((elapsedTime % 1) * 1000);
        timerText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));

    }

    // Method to stop the timer
    public void stop() {
        elapsedTime = -1;  // Stop the timer by setting elapsed time to -1
    }

    // Method to get the timer text for adding to the scene
    public Text getTimerText() {
        return timerText;
    }

    // Method to get the elapsed time
    public double getElapsedTime() {
        return elapsedTime;
    }
}

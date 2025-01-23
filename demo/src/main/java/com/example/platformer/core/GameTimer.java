package com.example.platformer.core;

import javafx.scene.text.Text;

public class GameTimer {
    private double elapsedTime = 0; // Elapsed time in seconds
    private final Text timerText;

    public GameTimer(int screenWidth, int sidebarWidth, int offsetX, int offsetY) {
        timerText = new Text();
        timerText.setId("game-timer"); // Assign a CSS ID for styling

        timerText.setX(screenWidth - sidebarWidth - offsetX);  // Position the timer text near the top-right corner
        timerText.setY(offsetY);
    }

    public void update(double deltaTime, double newYPosition) {
        if (elapsedTime < 0) return; // Stop if the game is finished
        elapsedTime += deltaTime;
        timerText.setY(newYPosition);
        displayTime();
    }

    // Method to display elapsed time in "mm:ss:SSS" format
    private void displayTime() {
        int minutes = (int) (elapsedTime / 60);
        int seconds = (int) (elapsedTime % 60);
        int milliseconds = (int) ((elapsedTime % 1) * 1000);
        timerText.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
    }

    // Stop the timer
    public void stop() {
        elapsedTime = -1;
    }

    // Getter for the Text node to add it to the scene
    public Text getTimerText() {
        return timerText;
    }

    // Getter for the elapsed time
    public double getElapsedTime() {
        return elapsedTime;
    }
}

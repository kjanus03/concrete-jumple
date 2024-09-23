package com.example.platformer;

import javafx.animation.AnimationTimer;

public abstract class GameLoop extends AnimationTimer {
    private long previousTime = 0;

    @Override
    public void handle(long now) {
        if (previousTime == 0) {
            previousTime = now;
            return;
        }

        // Calculate the time difference between frames (in nanoseconds)
        long deltaTime = now - previousTime;
        previousTime = now;

        // Update game state
        update(deltaTime / 1e9);  // Convert nanoseconds to seconds
    }

    // Method that each game should implement to handle updates
    protected abstract void update(double deltaTime);
}

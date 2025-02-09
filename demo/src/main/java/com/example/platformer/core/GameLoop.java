package com.example.platformer.core;

import javafx.animation.AnimationTimer;

public abstract class GameLoop extends AnimationTimer {
    private long previousTime = 0;

    @Override
    public void handle(long now) {
        if (previousTime == 0) {
            previousTime = now;
            return;
        }

        long deltaTime = now - previousTime;
        previousTime = now;

        update(deltaTime / 1e9);
    }

    protected abstract void update(double deltaTime);
}

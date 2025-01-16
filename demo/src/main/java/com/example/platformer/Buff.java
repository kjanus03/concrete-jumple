package com.example.platformer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Buff extends Entity {

    private int jumpBuff;
    private int duration;
    private Timeline timer; // Timer for this buff
    private Runnable onBuffEnd; // Action to perform when buff ends

    public Buff(double x, double y, int jumpBuff, int duration) {
        super(x, y, 30, 30);
        this.jumpBuff = jumpBuff;
        this.duration = duration;
        this.entityView.setStyle("-fx-fill: green;");
    }

    public int getJumpBuff() {
        return jumpBuff;
    }

    public int getDuration() {
        return duration;
    }

    public void startTimer(Runnable onBuffEnd) {
        this.onBuffEnd = onBuffEnd;
        timer = new Timeline(new KeyFrame(Duration.seconds(duration), event -> {
            if (this.onBuffEnd != null) {
                this.onBuffEnd.run();
            }
        }));
        timer.setCycleCount(1);
        timer.play();
    }

    public void stopTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public double getRemainingTime() {
        if (timer != null) {
            return Math.max(0, duration - timer.getCurrentTime().toSeconds());
        }
        return 0;
    }
}

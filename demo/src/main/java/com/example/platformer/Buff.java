package com.example.platformer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class Buff extends Entity {

    private int buffAmount;

    private int duration;
    private Timeline timer;
    private Runnable onBuffEnd; // action to perform when buff ends
    private BuffType type;

    public Buff(double x, double y, int buffAmount, int duration, BuffType type) {
        super(x, y, 30, 30);
        this.buffAmount = buffAmount;
        this.duration = duration;
        this.type = type;
        // color depnding on type
        if (type == BuffType.JUMP) {
            this.entityView.setFill(Paint.valueOf("green"));
        } else if (type == BuffType.SPEED) {
            this.entityView.setFill(Paint.valueOf("purple"));
        }
        else if (type == BuffType.INVINCIBILITY) {
            this.entityView.setFill(Paint.valueOf("orange"));
        }
    }

    public int getBuffAmount() {
        return buffAmount;
    }

    public BuffType getType() {
        return type;
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

package com.example.platformer;

import javafx.scene.shape.Rectangle;


public class Buff extends Entity {
    protected double x, y;
    private int jumpBuff;

    private int duration;

    public Buff(double x, double y, int jumpBuff, int duration) {
        super(x, y, 30, 30);
        this.x = x;
        this.y = y;
        this.jumpBuff = jumpBuff;
        this.duration = duration;
        this.entityView.setStyle("-fx-fill: green;");
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getJumpBuff() {
        return jumpBuff;
    }

    public int getDuration() {
        return duration;
    }
}

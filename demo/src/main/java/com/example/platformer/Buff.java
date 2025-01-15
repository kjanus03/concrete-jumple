package com.example.platformer;

import javafx.scene.shape.Rectangle;


public class Buff {
    protected double x, y;
    protected Rectangle buffView;
    private int jumpBuff;

    public Buff(double x, double y, double width, double height, int jumpBuff) {
        this.x = x;
        this.y = y;
        this.jumpBuff = jumpBuff;
        this.buffView = new Rectangle(width, height);
        this.buffView.setTranslateX(x);
        this.buffView.setTranslateY(y);
    }

    public Rectangle getView() {
        return buffView;
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
}

package com.example.platformer;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Platform {
    private Rectangle platformView;

    public Platform(double x, double y, double width, double height) {
        platformView = new Rectangle(width, height, Color.BROWN);
        platformView.setTranslateX(x);
        platformView.setTranslateY(y);
    }

    public Rectangle getView() {
        return platformView;
    }
}

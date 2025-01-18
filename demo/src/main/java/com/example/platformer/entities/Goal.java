package com.example.platformer.entities;

import com.example.platformer.map.Platform;
import javafx.scene.image.ImageView;

public class Goal extends Entity {
    protected double x, y;
    private ImageView spriteView;

    public Goal(double x, double y) {
        super(x, y, 10, 10);
        this.x = x;
        this.y = y;
        this.spriteView = new ImageView(getClass().getResource("/sprites/goal/goal.png").toExternalForm());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

    }

    public void generateGoal(Platform lastPlatform) {
        double x = lastPlatform.getX() + (lastPlatform.getWidth() / 2.0) - 15;
        double y = lastPlatform.getY() - 30;
        this.entityView.setTranslateX(x);
        this.entityView.setTranslateY(y);
        this.entityView.setOpacity(0);
        this.spriteView.setTranslateX(x-3);
        this.spriteView.setTranslateY(y-18);
    }
    public ImageView getSpriteView() {
        return spriteView;
    }
}

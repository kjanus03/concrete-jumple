package com.example.platformer;

public class Goal extends Entity{
    protected double x, y;

    public Goal(double x, double y) {
        super(x, y, 30, 30);
        this.x = x;
        this.y = y;
        this.entityView.setStyle("-fx-fill: yellow;");
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void generateGoal(Platform lastPlatform) {
        double x = lastPlatform.getX() + (lastPlatform.getWidth() / 2.0) - 15;
        double y = lastPlatform.getY() - 30;
        this.entityView.setTranslateX(x);
        this.entityView.setTranslateY(y);
    }
}

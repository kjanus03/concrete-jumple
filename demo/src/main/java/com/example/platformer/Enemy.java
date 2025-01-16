package com.example.platformer;

public class Enemy extends Entity {
    private Player target;
    private boolean isChasing;

    public Enemy(double x, double y, Player target) {
        super(x, y, 30, 30);  // Initialize a 30x30 rectangle for the enemy
        this.target = target;
        this.isChasing = false;
        entityView.setStyle("-fx-fill: red;");  // Give the enemy a different color
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        if (isChasing) {
            chasePlayer();
        }

        isChasing = target.hasCollisionCooldown() && isPlayerInRange();
    }

    private boolean isPlayerInRange() {
        return Math.abs(target.getY() - this.getView().getTranslateY()) < 180;
    }

    private void moveRight() {
        velocityX = 100;
    }

    private void moveLeft() {
        velocityX = -100;
    }

    private void chasePlayer() {
        if (target.getX() > this.getX()) {
            moveRight();
        } else if (target.getX() < this.getX()) {
            moveLeft();
        } else {
            stopHorizontalMovement();
        }
    }
}

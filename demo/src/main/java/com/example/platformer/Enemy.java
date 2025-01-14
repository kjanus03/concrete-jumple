package com.example.platformer;

public class Enemy extends Entity {
    private Player target;  // Enemy targets the player

    public Enemy(double x, double y, Player target) {
        super(x, y, 30, 30);  // Initialize a 30x30 rectangle for the enemy
        this.target = target;
        entityView.setStyle("-fx-fill: red;");  // Give the enemy a different color
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Simple AI: Move towards the player
        chasePlayer(deltaTime);
    }

    private void moveRight() {
        velocityX = 100;
    }

    private void moveLeft() {
        velocityX = -100;
    }

    private void chasePlayer(double deltaTime) {
        if (target.getX() > this.getX()) {
            moveRight();
        } else if (target.getX() < this.getX()) {
            moveLeft();
        } else {
            stopHorizontalMovement();
        }
    }
}

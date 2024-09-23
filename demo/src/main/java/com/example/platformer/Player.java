package com.example.platformer;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Player {
    private Rectangle playerView;  // Player's visual representation
    private double velocityY = 0;  // Vertical velocity for jump/fall
    private boolean canJump = false;  // Whether the player can jump
    private static final double GRAVITY = 800;  // Gravity acceleration

    public Player(double x, double y) {
        playerView = new Rectangle(30, 30, Color.BLUE);  // 30x30 px player character
        playerView.setTranslateX(x);
        playerView.setTranslateY(y);
    }

    public Rectangle getView() {
        return playerView;
    }

    public void update(double deltaTime) {
        // Apply gravity
        velocityY += GRAVITY * deltaTime;

        // Update player's Y position based on velocity
        playerView.setTranslateY(playerView.getTranslateY() + velocityY * deltaTime);

        // Collision detection, floor detection, etc., should be done here
        if (playerView.getTranslateY() > 500) {  // Ground collision at Y=500
            playerView.setTranslateY(500);
            canJump = true;
            velocityY = 0;
        }
    }

    public void jump() {
        if (canJump) {
            velocityY = -400;  // Jump force
            canJump = false;
        }
    }

    public void moveLeft() {
        playerView.setTranslateX(playerView.getTranslateX() - 5);
    }

    public void moveRight() {
        playerView.setTranslateX(playerView.getTranslateX() + 5);
    }

}

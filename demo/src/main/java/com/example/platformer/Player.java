package com.example.platformer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Player extends Entity {
    private boolean canJump = false;
    private double jumpForce;
    private double defaultJumpForce;  // The default jump force
    private Timeline buffTimer;  // Timer for the buff duration
    private boolean isTouchingWall = false;
    private boolean collisionCooldown = false; // Cooldown to temporarily disable movement
    private Timeline cooldownTimer;

    public Player(double x, double y) {
        super(x, y, 30, 30);  // Initialize a 30x30 rectangle for the player
        entityView.setStyle("-fx-fill: blue;");  // Add some color to differentiate
        jumpForce = 700;  // Set the initial jump force
        defaultJumpForce = jumpForce;  // Store the default jump force
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Check if player is still in the air and should be falling
        if (!canJump) {
            velocityY += GRAVITY * deltaTime;  // Apply gravity if the player is in the air
        }
    }

    public void jump() {
        if (canJump) {
            velocityY = -this.jumpForce;  // Apply jump force
            canJump = false;   // Disable jumping again until landed
        }
    }

    public void moveLeft() {
        velocityX = -200;
    }

    public void moveRight() {
        velocityX = 200;
    }

    public void stopMoving() {
        velocityX = 0;
    }

    @Override
    public void landOnPlatform(Platform platform) {
        super.landOnPlatform(platform);
        canJump = true;  // Allow jumping after landing on the platform
    }

    public void applyJumpBuff(Buff buff) {
        this.jumpForce += buff.getJumpBuff();

        // Stop any existing buff timer
        if (buffTimer != null) {
            buffTimer.stop();
        }

        // Start a new timer for the buff duration
        buffTimer = new Timeline(new KeyFrame(
                Duration.seconds(buff.getDuration()),
                event -> removeJumpBuff(buff)
        ));
        buffTimer.setCycleCount(1);
        buffTimer.play();
    }

    public void handleWallCollision() {
        if (!isTouchingWall) {
            isTouchingWall = true; // Set the flag to prevent repeated collisions

            // Reverse direction
            velocityX = -velocityX;
            System.out.println("Wall collision");
            System.out.println("VelocityX: " + velocityX);

            // Apply upward bounce
            velocityY -= 200;

            // Start a brief cooldown to disable input
            collisionCooldown = true;
            if (cooldownTimer != null) {
                cooldownTimer.stop();
            }
            cooldownTimer = new Timeline(new KeyFrame(
                    Duration.seconds(0.3), // Set the cooldown duration
                    event -> {
                        collisionCooldown = false; // Re-enable input after cooldown
                        resetWallCollision();
                    }
            ));
            cooldownTimer.setCycleCount(1);
            cooldownTimer.play();
        }
    }

    public void setX(double x) {
        this.x = x;
        entityView.setTranslateX(x);
    }

    public void setY(double y) {
        this.y = y;
        entityView.setTranslateY(y);
    }

    public boolean hasCollisionCooldown() {
        return collisionCooldown;
    }


    public void resetWallCollision() {
        isTouchingWall = false; // Reset the wall collision state
    }
    private void removeJumpBuff(Buff buff) {
        this.jumpForce -= buff.getJumpBuff();
        if (this.jumpForce < defaultJumpForce) {
            this.jumpForce = defaultJumpForce;  // Ensure the jump force doesn't drop below default
        }
    }
}

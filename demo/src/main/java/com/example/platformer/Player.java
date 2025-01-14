package com.example.platformer;

public class Player extends Entity {
    private boolean canJump = false;

    public Player(double x, double y) {
        super(x, y, 30, 30);  // Initialize a 30x30 rectangle for the player
        entityView.setStyle("-fx-fill: blue;");  // Add some color to differentiate
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
            velocityY = -700;  // Apply jump force
            canJump = false;   // Disable jumping again until landed
        }
    }

    public void moveLeft() {
        velocityX = -200;  // Move player left at a certain speed
    }

    // Move the player right
    public void moveRight() {
        velocityX = 200;   // Move player right at a certain speed
    }

    // Stop horizontal movement when no key is pressed
    public void stopMoving() {
        velocityX = 0;
    }

    @Override
    public void landOnPlatform(Platform platform) {
        super.landOnPlatform(platform);
        canJump = true;  // Allow jumping after landing on the platform
    }
}

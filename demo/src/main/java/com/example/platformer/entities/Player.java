package entities;

import map.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.ArrayList;

public class Player extends Entity {
    private final int defaultSpeed;
    private final double defaultJumpForce;
    private boolean canJump = false;
    private int speed;
    private boolean isInvincible;
    private double jumpForce;
    private boolean isTouchingWall = false;
    private boolean collisionCooldown = false; // Cooldown to temporarily disable movement
    private boolean wallCollisionCooldown = false; // Cooldown to temporarily disable movement
    private Timeline hitTimer;

    private final ArrayList<Buff> activeBuffs;

    public Player(double x, double y) {
        super(x, y, 30, 30);  // Initialize a 30x30 rectangle for the player
        entityView.setStyle("-fx-fill: blue;");  // Add some color to differentiate
        jumpForce = 750;  // Set the initial jump force
        speed = 200;  // Set the initial speed
        defaultSpeed = speed;  // Store the default speed
        defaultJumpForce = jumpForce;  // Store the default jump force
        activeBuffs = new ArrayList<>();
        isInvincible = false;
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
        velocityX = -this.speed;
    }

    public void moveRight() {
        velocityX = this.speed;
    }

    public void stopMoving() {
        velocityX = 0;
    }

    @Override
    public void landOnPlatform(Platform platform) {
        super.landOnPlatform(platform);
        canJump = true;  // Allow jumping after landing on the platform
    }

    public void applyBuff(Buff buff) {
        switch (buff.getType()) {
            case JUMP -> this.jumpForce += buff.getBuffAmount();
            case SPEED -> this.speed += buff.getBuffAmount();
            case INVINCIBILITY -> this.isInvincible = true;
        }

        this.activeBuffs.add(buff);

        buff.startTimer(() -> {
            removeBuff(buff);
        });
    }

    private void removeBuff(Buff buff) {
        switch (buff.getType()) {
            case JUMP -> this.jumpForce = defaultJumpForce;
            case SPEED -> this.speed = defaultSpeed;
            case INVINCIBILITY -> this.isInvincible = false;
        }
        this.activeBuffs.remove(buff);
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
            wallCollisionCooldown = true;

            Timeline cooldownTimer = new Timeline(new KeyFrame(Duration.seconds(0.3), // Set the cooldown duration
                    event -> {
                        wallCollisionCooldown = false; // Re-enable input after cooldown
                        resetWallCollision();
                    }));
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

    public void enemyCollision() {
        System.out.println("Player hit by enemy");
        // player stopped for 2 seconds and the enemy gets deleted
        velocityX = 0;
        velocityY = 0;
        collisionCooldown = true;
        this.hitTimer = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            collisionCooldown = false;
        }));
        hitTimer.setCycleCount(1);
        hitTimer.play();
    }

    public boolean hasCollisionCooldown() {
        return !collisionCooldown && !wallCollisionCooldown;
    }

    public void resetWallCollision() {
        isTouchingWall = false; // Reset the wall collision state
    }

    public ArrayList<Buff> getActiveBuffs() {
        return activeBuffs;
    }

    public double getRemainingCooldownTime() {
        if (hitTimer != null) {
            return Math.max(0, 2 - hitTimer.getCurrentTime().toSeconds());
        }
        return 0;
    }

    public boolean isInvincible() {
        return isInvincible;
    }
}

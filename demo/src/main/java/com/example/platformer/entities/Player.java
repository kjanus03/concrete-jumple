package com.example.platformer.entities;

import com.example.platformer.map.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    // Sprite-related fields
    private final ImageView spriteView; // ImageView for displaying the sprite
    private final Image[] idleSprites; // Array of idle sprite images
    private Timeline idleAnimation; // Timeline for idle animation
    private boolean isIdle = true; // Track if the player is idle

    public Player(double x, double y) {
        super(x, y, 32, 54);  // Initialize a rectangle for the

        // Load idle sprites
        idleSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/main/idle_0.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/idle_1.png").toExternalForm())

        };
        spriteView = new ImageView(idleSprites[0]); // Start with the first idle sprite
        spriteView.setFitWidth(32); // Match entity size
        spriteView.setFitHeight(54);

        // make entity view transparent
        entityView.setOpacity(0);

        jumpForce = 750;  // Set the initial jump force
        speed = 200;  // Set the initial speed
        defaultSpeed = speed;  // Store the default speed
        defaultJumpForce = jumpForce;  // Store the default jump force
        activeBuffs = new ArrayList<>();
        isInvincible = false;

        setupIdleAnimation();
    }

    private void setupIdleAnimation() {
        // Create a timeline to cycle through idle sprites
        idleAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    // Cycle between the two idle sprites
                    if (spriteView.getImage() == idleSprites[0]) {
                        spriteView.setImage(idleSprites[1]);
                    } else {
                        spriteView.setImage(idleSprites[0]);
                    }
                })
        );
        idleAnimation.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Check if the player is idle (not moving)
        if (velocityX == 0 && canJump) {
            if (!isIdle) {
                isIdle = true;
                idleAnimation.play(); // Start idle animation
            }
        } else {
            if (isIdle) {
                isIdle = false;
                idleAnimation.stop(); // Stop idle animation
                spriteView.setImage(idleSprites[0]); // Reset to first frame
            }
        }

        // Apply gravity if the player is in the air
        if (!canJump) {
            velocityY += GRAVITY * deltaTime;
        }

        spriteView.setTranslateX(x);
        spriteView.setTranslateY(y);
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
            if (velocityX >= 0) {
                x -= 5; // Move the player back to prevent sticking to the wall
            } else {
                x += 5; // Move the player back to prevent sticking to the wall
            }

            // Reverse direction
            velocityX = -velocityX;
            // Apply upward bounce
            velocityY -= 300;

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
        spriteView.setTranslateX(x);
    }

    public void setY(double y) {
        this.y = y;
        entityView.setTranslateY(y);
        spriteView.setTranslateY(y);
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
        isTouchingWall = false;
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

    public ImageView getSpriteView() {
        return spriteView;
    }
}

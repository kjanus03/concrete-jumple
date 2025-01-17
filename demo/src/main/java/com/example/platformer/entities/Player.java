package com.example.platformer.entities;

import com.example.platformer.map.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

import static java.lang.Math.abs;

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
    private Image[] idleSprites; // Array of idle sprite images
    private Image idleRightSprite;
    private Image idleLeftSprite;
    private Image[] walkRightSprites;
    private Image[] walkLeftSprites;
    private Image[] jumpRightSprites;
    private Image[] jumpLeftSprites;
    private boolean isIdle = true; // Track if the player is idle
    private boolean isInAir = false;
    private boolean onPlatform = true;
    private String direction; // "right" or "left"

    private Timeline currentAnimation;
    private enum PlayerState {
        IDLE,
        IDLE_RIGHT,
        IDLE_LEFT,
        WALKING,
        JUMPING_STRAIGHT,
        JUMPING_RIGHT_UP,
        JUMPING_RIGHT_FLAT,
        JUMPING_LEFT_UP,
        JUMPING_LEFT_FLAT,
        FREEZE,
        FALLING_LEFT,
        FALLING_RIGHT,
        FALLING_STRAIGHT
    }

    private PlayerState currentState = null;


    public Player(double x, double y) {
        super(x, y, 32, 64);  // Initialize a rectangle for the
        loadAnimations();
        // Load idle sprites

        spriteView = new ImageView(idleSprites[0]); // Start with the first idle sprite
        spriteView.setFitWidth(32); // Match entity size
        spriteView.setFitHeight(64);

        // make entity view transparent
        entityView.setOpacity(0);



        jumpForce = 750;  // Set the initial jump force
        speed = 200;  // Set the initial speed
        defaultSpeed = speed;  // Store the default speed
        defaultJumpForce = jumpForce;  // Store the default jump force
        activeBuffs = new ArrayList<>();
        isInvincible = false;
    }

    private void loadAnimations() {
        // Idle animations
        idleSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/main/idle_0.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/idle_left.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/idle_1.png").toExternalForm()),
        new Image(getClass().getResource("/sprites/characters/main/idle_right.png").toExternalForm())};


        idleRightSprite = new Image(getClass().getResource("/sprites/characters/main/idle_right.png").toExternalForm());
        idleLeftSprite = new Image(getClass().getResource("/sprites/characters/main/idle_left.png").toExternalForm());

        // Walking animations
        walkRightSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/main/walk_right_1.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/walk_right_2.png").toExternalForm())
        };

        walkLeftSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/main/walk_left_1.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/walk_left_2.png").toExternalForm())
        };

        // Jumping animations
        jumpRightSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/main/jump_straight.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/jump_right_up.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/jump_right_flat.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/fall_right.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/fall_straight.png").toExternalForm())
        };
        jumpLeftSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/main/jump_straight.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/jump_left_up.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/jump_left_flat.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/fall_left.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/main/fall_straight.png").toExternalForm())
        };
    }



    private void setState(PlayerState newState) {
        if (currentState == newState) {
            return; // Avoid redundant state changes
        }

        currentState = newState;
        if (currentAnimation != null) {
            currentAnimation.stop(); // Stop any running animation
        }

        switch (newState) {
            case IDLE:
                setupIdleAnimation();
                break;
            case WALKING:
                if ("right".equals(direction)) {
                    setupWalkingRightAnimation();
                } else {
                    setupWalkingLeftAnimation();
                }
                break;
            case JUMPING_RIGHT_FLAT:
                spriteView.setImage(jumpRightSprites[2]); // Static image for falling left
                break;
            case JUMPING_RIGHT_UP:
                spriteView.setImage(jumpRightSprites[1]); // Static image for falling left
                break;
            case JUMPING_LEFT_FLAT:
                spriteView.setImage(jumpLeftSprites[2]); // Static image for falling left
                break;
            case JUMPING_STRAIGHT:
                spriteView.setImage(jumpLeftSprites[0]); // Static image for falling left
                break;
            case JUMPING_LEFT_UP:
                spriteView.setImage(jumpLeftSprites[1]); // Static image for falling left
                break;
            case FALLING_LEFT:
                spriteView.setImage(jumpLeftSprites[3]); // Static image for falling left
                break;
            case FALLING_RIGHT:
                spriteView.setImage(jumpRightSprites[3]); // Static image for falling right
                break;
            case FALLING_STRAIGHT:
                spriteView.setImage(jumpRightSprites[4]); // Static image for falling straight (use the same sprite for both directions if needed)
                break;
            case FREEZE:
                return;
        }

        if (currentAnimation != null) {
            currentAnimation.play();
        }
    }


    private void setupIdleAnimation() {
        // Create a timeline to cycle through idle sprites
        currentAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (spriteView.getImage() == idleSprites[0]) {
                        spriteView.setImage(idleSprites[2]);
                    } else {
                        spriteView.setImage(idleSprites[0]);
                    }
                })
        );
        currentAnimation.setCycleCount(Timeline.INDEFINITE);
    }
    private void setupWalkingRightAnimation() {
        spriteView.setImage(walkRightSprites[0]); // Immediately show the first frame
        currentAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.25), event -> {
                    if (spriteView.getImage() == walkRightSprites[0]) {
                        spriteView.setImage(walkRightSprites[1]);
                    } else {
                        spriteView.setImage(walkRightSprites[0]);
                    }
                })
        );
        currentAnimation.setCycleCount(Timeline.INDEFINITE);
    }

    private void setupWalkingLeftAnimation() {
        spriteView.setImage(walkLeftSprites[0]); // Immediately show the first frame
        currentAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.25), event -> {
                    if (spriteView.getImage() == walkLeftSprites[0]) {
                        spriteView.setImage(walkLeftSprites[1]);
                    } else {
                        spriteView.setImage(walkLeftSprites[0]);
                    }
                })
        );
        currentAnimation.setCycleCount(Timeline.INDEFINITE);
    }









    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);

        // Determine the current state
        if (collisionCooldown) {
            setState(PlayerState.FREEZE);
        } else if (isInAir){
            if (velocityY > -750 && velocityY <= -500) {
                // Jumping straight
                setState(PlayerState.JUMPING_STRAIGHT);
            } else if (velocityY > -500 && velocityY <= -150) {
                // Jumping up
                if ("right".equals(direction)) {
                    setState(PlayerState.JUMPING_RIGHT_UP);
                } else if ("left".equals(direction)) {
                    setState(PlayerState.JUMPING_LEFT_UP);
                }
            } else if (velocityY > -150 && velocityY <= 150) {
                // Jumping flat
                if ("right".equals(direction)) {
                    setState(PlayerState.JUMPING_RIGHT_FLAT);
                } else if ("left".equals(direction)) {
                    setState(PlayerState.JUMPING_LEFT_FLAT);
                }
            } else if (velocityY > 150 && velocityY <= 500) {
                // Falling
                if ("right".equals(direction)) {
                    setState(PlayerState.FALLING_RIGHT);
                } else if ("left".equals(direction)) {
                    setState(PlayerState.FALLING_LEFT);
                }
            } else if (velocityY > 500 && velocityY <= 750) {
                // Falling straight
                setState(PlayerState.FALLING_STRAIGHT);
            }
        }
        if (abs(velocityX) >= 0.05 && isInAir == false) { // Walking
            setState(PlayerState.WALKING);
        } else {
            setState(PlayerState.IDLE);
        }

        // Apply gravity if the player is in the air
        if (!canJump) {
            velocityY += GRAVITY * deltaTime;
        }
        // Update sprite position
        spriteView.setTranslateX(x);
        spriteView.setTranslateY(y);
    }




    public void jump() {
        if (canJump) {
            velocityY = -this.jumpForce;  // Apply jump force
            canJump = false;   // Disable jumping again until landed
            isInAir = true;

        }
    }

    public void moveLeft() {
        direction = "left";
        velocityX = -this.speed;
    }

    public void moveRight() {
        direction = "right";
        velocityX = this.speed;
    }

    public void stopMoving() {
        velocityX = 0;
    }

    @Override
    public void landOnPlatform(Platform platform) {
        super.landOnPlatform(platform);
        canJump = true;  // Allow jumping after landing on the platform
        onPlatform = true;
        isInAir = false;
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
            if (direction == "left"){
                direction = "right";}
            } else {direction = "left";}
            // Apply upward bounce
            velocityY -= 300;

            // Start a brief cooldown to disable input
            wallCollisionCooldown = true;

            Timeline cooldownTimer = new Timeline(new KeyFrame(Duration.seconds(0.5), // Set the cooldown duration
                    event -> {
                        wallCollisionCooldown = false; // Re-enable input after cooldown
                        resetWallCollision();
                    }));
            cooldownTimer.setCycleCount(1);
            cooldownTimer.play();
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
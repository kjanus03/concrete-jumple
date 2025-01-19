package com.example.platformer.entities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Enemy extends Entity {
    private Player target;
    private boolean isChasing;
    private Image[] walkRightSprites;
    private Image[] walkLeftSprites;
    private String direction; // right or left
    private final ImageView spriteView; // ImageView for displaying the sprite

    private Image[] idleSprites; // Array of idle sprite images
    private Timeline currentAnimation;
    private boolean idle = true;





    public Enemy(double x, double y, Player target) {
        super(x, y, 32, 32);  // Initialize a 30x30 rectangle for the enemy
        loadAnimations();
        this.target = target;
        this.isChasing = false;

        spriteView = new ImageView(idleSprites[0]); // Start with the first idle sprite
        spriteView.setFitWidth(32); // Match entity size
        spriteView.setFitHeight(32);
        entityView.setOpacity(0);

    }
    private void loadAnimations() {
        // Idle animations
        idleSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/enemy/idle_left_0.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/enemy/idle_right_0.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/enemy/idle_left_1.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/enemy/idle_right_1.png").toExternalForm())};
        walkRightSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/enemy/right_0.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/enemy/right_1.png").toExternalForm())
        };
        walkLeftSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/enemy/left_1.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/enemy/left_0.png").toExternalForm())
        };
    }


    private void setupIdleAnimation() {
        // Create a timeline to cycle through idle sprites
        currentAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (spriteView.getImage() == idleSprites[0]) {
                        spriteView.setImage(idleSprites[1]);
                    } else {
                        spriteView.setImage(idleSprites[0]);
                    }
                })
        );
        currentAnimation.setCycleCount(Timeline.INDEFINITE);
    }
    private void changeAnimation() {
        // Stop the current animation

        if (isChasing) {
            // Change animation to walking based on direction
            if ("right".equals(direction)) {
                setupWalkingRightAnimation();
            } else if ("left".equals(direction)) {
                setupWalkingLeftAnimation();
            }
        } else {
            // If not chasing, return to idle animation
            setupIdleAnimation();
        }

        // Start the new animation
        if (currentAnimation != null) {
            currentAnimation.play();
        }
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

        // Update chasing state and animations
        boolean wasChasing = isChasing;
        isChasing = target.hasCollisionCooldown() && isPlayerInRange();

        if (isChasing) {
            chasePlayer();
        } else {
            stopHorizontalMovement();
            idle = true;
        }

        // If the chasing state has changed, update the animation
        if (wasChasing != isChasing) {
            changeAnimation();
        }
        spriteView.setTranslateX(x);
        spriteView.setTranslateY(y);
    }


    private boolean isPlayerInRange() {
        return Math.abs(target.getY() - this.getView().getTranslateY()) < 180;
    }

    private void moveRight() {

        direction = "right";
        velocityX = 100;
    }

    private void moveLeft() {
        direction = "left";
        velocityX = -100;
    }

    private void chasePlayer() {
        if (target.getX() > this.getX()) {
            direction = "right";
            moveRight();
        } else if (target.getX() < this.getX()) {
            direction = "left";
            moveLeft();
        } else {
            stopHorizontalMovement();
        }
    }
}

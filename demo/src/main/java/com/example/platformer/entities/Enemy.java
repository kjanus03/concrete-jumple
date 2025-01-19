package com.example.platformer.entities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Enemy extends Entity {
    private Player target;
    private boolean isChasing;
    private Image[] idleSprites; // Array of idle sprite images
    private Image[] walkRightSprites;
    private Image[] walkLeftSprites;
    private Timeline currentAnimation;
    private final ImageView spriteView;
    private String direction;

    public Enemy(double x, double y, Player target) {
        super(x, y, 64, 64);  // Initialize a 30x30 rectangle for the enemy
        this.target = target;
        this.isChasing = false;
        loadAnimations();

        entityView.setOpacity(0);

        spriteView = new ImageView(idleSprites[0]);
        spriteView.setFitWidth(64); // Match entity size
        spriteView.setFitHeight(64);

        this.spriteView.setImage(idleSprites[0]);

        setupIdleAnimation();
        currentAnimation.play();
    }
    private void loadAnimations() {
        // Idle animations
        idleSprites = new Image[]{
                new Image(getClass().getResource("/sprites/characters/enemy/idle_0.png").toExternalForm()),
                new Image(getClass().getResource("/sprites/characters/enemy/idle_1.png").toExternalForm())};
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
    private void setupWalkingRightAnimation() {
        currentAnimation.stop();
        spriteView.setImage(walkRightSprites[0]); // Immediately show the first frame
        currentAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
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
        currentAnimation.stop();
        spriteView.setImage(walkLeftSprites[0]); // Immediately show the first frame
        currentAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.5), event -> {
                    if (spriteView.getImage() == walkLeftSprites[0]) {
                        spriteView.setImage(walkLeftSprites[1]);
                    } else {
                        spriteView.setImage(walkLeftSprites[0]);
                    }
                })
        );
        currentAnimation.setCycleCount(Timeline.INDEFINITE);

    }




    private boolean isPlayerInRange() {
        return Math.abs(target.getY() - this.getView().getTranslateY()) < 180;
    }

    private void moveRight() {

        direction = "right";
        velocityX = 100;
        if (direction != "right"){
            setupWalkingRightAnimation();
            currentAnimation.play();
        }
        direction = "right";
    }

    private void moveLeft() {
        if (direction != "left") {
            setupWalkingLeftAnimation();
            currentAnimation.play();

            velocityX = -100;
            direction = "left";
        }
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

    public ImageView getSpriteView() {
        return spriteView;
    }
}

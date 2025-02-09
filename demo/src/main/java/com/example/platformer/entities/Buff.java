package com.example.platformer.entities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;

public class Buff extends Entity {
    private static final int SPRITE_WIDTH = 28;
    private static final int SPRITE_HEIGHT = 25;

    private final int buffAmount;
    private final Image[] buffSprites;
    private final ImageView spriteView;
    private int duration;
    private Timeline timer;
    private Runnable onBuffEnd; // action to perform when buff ends
    private final BuffType type;
    private Timeline currentAnimation;

    public Buff(double x, double y, int buffAmount, BuffType type, int scalingFactor) {
        super(x, y, SPRITE_WIDTH * scalingFactor, SPRITE_HEIGHT * scalingFactor);
        this.buffAmount = buffAmount;
        this.type = type;
        this.buffSprites = new Image[2];
        loadAnimations();

        spriteView = new ImageView(buffSprites[0]);
        spriteView.setFitWidth(SPRITE_WIDTH * scalingFactor); // Match entity size
        spriteView.setFitHeight(SPRITE_HEIGHT * scalingFactor);

//        spriteView.setTranslateY(-32*scalingFactor);

        // make entity view transparent
        entityView.setOpacity(0);
        // color and duration depnding on type
        this.spriteView.setImage(buffSprites[0]);
        if (type == BuffType.JUMP) {
            this.duration = 2;

        } else if (type == BuffType.SPEED) {
            this.duration = 4;
        } else if (type == BuffType.INVINCIBILITY) {
            this.duration = 6;
        }
        setupAnimation();
        currentAnimation.play();
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        this.spriteView.setTranslateX(x);
        this.spriteView.setTranslateY(y);
    }

    public int getBuffAmount() {
        return buffAmount;
    }

    public BuffType getType() {
        return type;
    }

    public void startTimer(Runnable onBuffEnd) {
        this.onBuffEnd = onBuffEnd;
        timer = new Timeline(new KeyFrame(Duration.seconds(duration), event -> {
            if (this.onBuffEnd != null) {
                this.onBuffEnd.run();
            }
        }));
        timer.setCycleCount(1);
        timer.play();
    }

    public double getRemainingTime() {
        if (timer != null) {
            return Math.max(0, duration - timer.getCurrentTime().toSeconds());
        }
        return 0;
    }

    private void loadAnimations() {
        if (type == BuffType.JUMP) {
            buffSprites[0] = new Image(Objects.requireNonNull(getClass().getResource("/sprites/buffs/jump_buff_0.png")).toExternalForm());
            buffSprites[1] = new Image(Objects.requireNonNull(getClass().getResource("/sprites/buffs/jump_buff_1.png")).toExternalForm());
        } else if (type == BuffType.SPEED) {
            buffSprites[0] = new Image(Objects.requireNonNull(getClass().getResource("/sprites/buffs/speed_buff_0.png")).toExternalForm());
            buffSprites[1] = new Image(Objects.requireNonNull(getClass().getResource("/sprites/buffs/speed_buff_1.png")).toExternalForm());
        } else if (type == BuffType.INVINCIBILITY) {
            buffSprites[0] = new Image(Objects.requireNonNull(getClass().getResource("/sprites/buffs/invincibility_buff_0.png")).toExternalForm());
            buffSprites[1] = new Image(Objects.requireNonNull(getClass().getResource("/sprites/buffs/invincibility_buff_1.png")).toExternalForm());
        }
    }

    private void setupAnimation() {
        currentAnimation = new Timeline(new KeyFrame(Duration.seconds(0.5), event -> {
            if (spriteView.getImage() == buffSprites[0]) {
                spriteView.setImage(buffSprites[1]);
            } else {
                spriteView.setImage(buffSprites[0]);
            }
        }));
        currentAnimation.setCycleCount(Timeline.INDEFINITE);
    }

    public ImageView getSpriteView() {
        return spriteView;
    }
}

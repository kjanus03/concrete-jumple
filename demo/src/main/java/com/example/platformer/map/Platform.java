package com.example.platformer.map;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Platform {
    private Group platformView; // Group to hold all parts of the platform
    private int x, y;

    public Platform(double x, double y, int width, int height) {
        platformView = new Group(); // Initialize the container for platform parts

        // Load the images for the platform parts
        Image platformImageLeft = new Image(getClass().getResource("/sprites/platforms/platform_left.png").toExternalForm());
        Image platformImageMiddle = new Image(getClass().getResource("/sprites/platforms/platform_middle.png").toExternalForm());
        Image platformImageRight = new Image(getClass().getResource("/sprites/platforms/platform_right.png").toExternalForm());

        // Calculate scaling factors for the sprites
        double originalSpriteHeight = 32; // Assume original height of the sprite is 16px
        double scalingFactor = height / originalSpriteHeight; // Scaling factor for height

        // Scale the left part
        ImageView leftPart = new ImageView(platformImageLeft);
        leftPart.setFitWidth(platformImageLeft.getWidth() * scalingFactor); // Scale width proportionally
        leftPart.setFitHeight(height); // Set height to match scaling factor
        leftPart.setTranslateX(0); // Position the left part
        platformView.getChildren().add(leftPart);

        // Calculate middle part dimensions
        double middleWidth = platformImageMiddle.getWidth() * scalingFactor; // Scale middle part width
        int numMiddleParts = (int) ((width - leftPart.getFitWidth() - platformImageRight.getWidth() * scalingFactor) / middleWidth);

        // Add middle parts
        for (int i = 0; i < numMiddleParts; i++) {
            ImageView middlePart = new ImageView(platformImageMiddle);
            middlePart.setFitWidth(middleWidth);
            middlePart.setFitHeight(height); // Match the scaled height
            middlePart.setTranslateX(leftPart.getFitWidth() + i * middleWidth);
            platformView.getChildren().add(middlePart);
        }

        // Scale the right part
        ImageView rightPart = new ImageView(platformImageRight);
        rightPart.setFitWidth(platformImageRight.getWidth() * scalingFactor); // Scale width proportionally
        rightPart.setFitHeight(height); // Set height to match scaling factor
        rightPart.setTranslateX(leftPart.getFitWidth() + numMiddleParts * middleWidth);
        platformView.getChildren().add(rightPart);

        // Position the entire platform
        this.x = (int) x;
        this.y = (int) y;
        platformView.setTranslateX(x);
        platformView.setTranslateY(y);
    }

    public Group getView() {
        return platformView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return (int) platformView.getBoundsInParent().getWidth();
    }

    public int getHeight() {
        return (int) platformView.getBoundsInParent().getHeight();
    }
}


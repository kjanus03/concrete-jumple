package com.example.platformer.map;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Platform {
    private Group platformView; // Group to hold all parts of the platform
    private double x, y;
    private int platformSpriteHeight = 32;

    public Platform(double x, double y, double width, double height) {
        platformView = new Group(); // Initialize the container for platform parts

        // Load the images for the platform parts
        Image platformImageLeft = new Image(getClass().getResource("/sprites/platforms/platform_left.png").toExternalForm());
        Image platformImageMiddle = new Image(getClass().getResource("/sprites/platforms/platform_middle.png").toExternalForm());
        Image platformImageRight = new Image(getClass().getResource("/sprites/platforms/platform_right.png").toExternalForm());

        // Create ImageView for the left part
        ImageView leftPart = new ImageView(platformImageLeft);
        leftPart.setFitWidth(platformSpriteHeight); // Adjust width if needed
        leftPart.setFitHeight(platformSpriteHeight); // Adjust height
        leftPart.setTranslateX(0);

        // Add the left part to the group
        platformView.getChildren().add(leftPart);

        // Calculate the number of middle parts based on width
        double middleWidth = platformImageMiddle.getWidth();
        int numMiddleParts = (int) ((width - platformImageLeft.getWidth() - platformImageRight.getWidth()) / middleWidth);

        // Add middle parts to the group
        for (int i = 0; i < numMiddleParts; i++) {
            ImageView middlePart = new ImageView(platformImageMiddle);
            middlePart.setFitWidth(middleWidth);
            middlePart.setFitHeight(platformSpriteHeight);
            middlePart.setTranslateX(platformImageLeft.getWidth() + i * middleWidth);
            platformView.getChildren().add(middlePart);
        }

        // Create ImageView for the right part
        ImageView rightPart = new ImageView(platformImageRight);
        rightPart.setFitWidth(platformSpriteHeight); // Adjust width if needed
        rightPart.setFitHeight(platformSpriteHeight); // Adjust height
        rightPart.setTranslateX(platformImageLeft.getWidth() + numMiddleParts * middleWidth);

        // Add the right part to the group
        platformView.getChildren().add(rightPart);

        // Position the entire platform
        this.x = x;
        this.y = y;
        platformView.setTranslateX(x);
        platformView.setTranslateY(y);
    }

    public Group getView() {
        return platformView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return (int) platformView.getBoundsInParent().getWidth();
    }
}

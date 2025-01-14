package com.example.platformer;

import javafx.scene.shape.Rectangle;

public class CollisionManager {

    // Method to check if an entity is standing on top of a platform
    public boolean isEntityOnPlatform(Entity entity, Platform platform) {
        Rectangle entityBounds = entity.getView();
        Rectangle platformBounds = platform.getView();

        return entityBounds.getBoundsInParent().intersects(platformBounds.getBoundsInParent()) &&
                entityBounds.getTranslateY() + entityBounds.getHeight() >= platformBounds.getTranslateY();
    }

    // Method to check if an entity is colliding with the side of a platform
    public boolean isEntityCollidingSide(Entity entity, Platform platform) {
        Rectangle entityBounds = entity.getView();
        Rectangle platformBounds = platform.getView();

        // Check if the entity is intersecting the platform on the sides
        return entityBounds.getBoundsInParent().intersects(platformBounds.getBoundsInParent()) &&
                !(entityBounds.getTranslateY() + entityBounds.getHeight() <= platformBounds.getTranslateY());
    }

    // Method to analyze and handle collisions for any entity
    public void analyzeEntityCollisions(Entity entity, Platform platform) {
        if (isEntityOnPlatform(entity, platform)) {
            entity.landOnPlatform(platform);
        } else if (isEntityCollidingSide(entity, platform)) {
            entity.stopHorizontalMovement();
        }
    }
}

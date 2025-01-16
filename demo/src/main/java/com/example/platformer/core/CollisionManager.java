package com.example.platformer.core;

import com.example.platformer.entities.Entity;
import com.example.platformer.entities.Player;
import javafx.scene.shape.Rectangle;
import com.example.platformer.map.Platform;

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

    public boolean isEntityCollidingWalls(Entity entity) {
        Rectangle entityBounds = entity.getView();
        boolean colliding = entityBounds.getTranslateX() <= 0 ||
                entityBounds.getTranslateX() + entityBounds.getWidth() >= 800;

        if (!colliding && entity instanceof Player) {
            ((Player) entity).resetWallCollision(); // Reset collision state if not colliding
        }
        return colliding;
    }


    // Method to analyze and handle collisions for any entity
    public void analyzeEntityCollisions(Entity entity, Platform platform) {
        if (isEntityOnPlatform(entity, platform)) {
            entity.landOnPlatform(platform);
        } else if (isEntityCollidingSide(entity, platform)) {
            entity.stopHorizontalMovement();
        }
    }

    public boolean areEntitiesColliding(Entity entity1, Entity entity2) {
        Rectangle entity1Bounds = entity1.getView();
        Rectangle entity2Bounds = entity2.getView();

        return entity1Bounds.getBoundsInParent().intersects(entity2Bounds.getBoundsInParent());
    }
}

package entities;

import map.Platform;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
    protected Rectangle entityView;
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected double x, y;  // Position in the world
    protected static final double GRAVITY = 800;  // Shared gravity

    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.entityView = new Rectangle(width, height);
        this.entityView.setTranslateX(x);
        this.entityView.setTranslateY(y);
    }

    // Method to update the entity's position based on velocity
    public void update(double deltaTime) {
        // Apply velocity to position
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        // Apply gravity (if applicable)
        velocityY += GRAVITY * deltaTime;

        // Update the visual representation
        entityView.setTranslateX(x);
        entityView.setTranslateY(y);
    }

    // Method to be called when the entity lands on a platform
    public void landOnPlatform(Platform platform) {
        this.y = platform.getView().getTranslateY() - entityView.getHeight();  // Set entity on top of the platform
        velocityY = 0;  // Stop falling
    }

    // Method to stop horizontal movement (e.g., when colliding with a platform's side)
    public void stopHorizontalMovement() {
        velocityX = 0;
    }

    public Rectangle getView() {
        return entityView;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

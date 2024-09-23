package com.example.platformer;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class GameController extends GameLoop {
    private Player player;
    private Pane gameRoot;
    private Set<KeyCode> activeKeys;  // Track currently pressed keys

    public GameController(Pane root) {
        this.gameRoot = root;
        this.activeKeys = new HashSet<>();  // Initialize the set for tracking active keys
    }

    public void startGame(Scene scene) {
        // Initialize player and level
        player = new Player(50, 50);  // Player starts at (50, 50)
        gameRoot.getChildren().add(player.getView());

        // Create key listeners for movement
        addKeyListeners(scene);

        // Start the game loop
        start();
    }

    // Add key press and release listeners
    private void addKeyListeners(Scene scene) {
        // When a key is pressed, add it to the set of active keys
        scene.setOnKeyPressed(event -> {
            activeKeys.add(event.getCode());
        });

        // When a key is released, remove it from the set of active keys
        scene.setOnKeyReleased(event -> {
            activeKeys.remove(event.getCode());
        });
    }

    @Override
    protected void update(double deltaTime) {
        // Update player movements based on active keys
        handleKeyPresses();

        // Update player (gravity, etc.)
        player.update(deltaTime);

        // Handle other game mechanics (collision, etc.)
    }

    // Handle movement based on pressed keys
    private void handleKeyPresses() {
        if (activeKeys.contains(KeyCode.LEFT)) {
            player.moveLeft();
        }
        if (activeKeys.contains(KeyCode.RIGHT)) {
            player.moveRight();
        }
        if (activeKeys.contains(KeyCode.SPACE)) {
            player.jump();
        }
    }
}

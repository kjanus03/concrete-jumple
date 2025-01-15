package com.example.platformer;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.List;

public class GameController extends GameLoop {
    private Player player;
    private ArrayList<Enemy> enemies;
    private Map map;  // The Map that holds all platforms
    private Pane gameRoot;
    private CollisionManager collisionManager;  // Use CollisionManager for all collision analysis
    private List<Platform> platformsWithEnemies;

    private EnemyGenerator enemyGenerator;

    public GameController(Pane root, Scene scene) {
        this.gameRoot = root;
        this.collisionManager = new CollisionManager();
        this.platformsWithEnemies = new ArrayList<>();
        this.enemies = new ArrayList<>();
        setupInputHandling(scene);  // Setup keyboard input handling
    }

    public void startGame() {
        // Initialize player
        player = new Player(50, 550);  // Start player slightly above the ground platform
        gameRoot.getChildren().add(player.getView());

        // Initialize the map, which will create and add platforms to the game root
        map = new Map(gameRoot);

        // Generate enemies
        enemyGenerator = new EnemyGenerator(map, 0.5, player);
        enemies = enemyGenerator.generateEnemies();

        for (Enemy enemy : enemies) {
            gameRoot.getChildren().add(enemy.getView());
        }


        // Start the game loop
        start();
    }

    @Override
    protected void update(double deltaTime) {
        // Update player and enemies
        player.update(deltaTime);

        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
        }

        // Handle collisions between player, enemies, and platforms
        handleCollisions();
        // Follow the player's vertical position with the camera
        if (player.getY() < gameRoot.getHeight() / 2) {
            followPlayer();
        }

    }

    private void handleCollisions() {
        // Get platforms from the map and check for collisions
        List<Platform> platforms = map.getPlatforms();

        // Check collisions for player
        for (Platform platform : platforms) {
            collisionManager.analyzeEntityCollisions(player, platform);
        }

        // Check collisions for each enemy
        for (Enemy enemy : enemies) {
            for (Platform platform : platforms) {
                collisionManager.analyzeEntityCollisions(enemy, platform);
            }
        }
    }

    // Handle keyboard input for player movement and jumping
    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                player.moveLeft();  // Move player left
            } else if (event.getCode() == KeyCode.RIGHT) {
                player.moveRight();  // Move player right
            } else if (event.getCode() == KeyCode.SPACE) {
                player.jump();  // Make player jump if possible
            }
        });

        scene.setOnKeyReleased(event -> {
            // Stop horizontal movement when left or right keys are released
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                player.stopMoving();
            }
        });
    }

    // Method to follow the player's vertical position with the camera
    public void followPlayer() {
        double playerY = player.getY();
        double offset = playerY - gameRoot.getHeight() / 2;  // Offset to center the player on the screen
        gameRoot.setTranslateY(-offset);
    }

}

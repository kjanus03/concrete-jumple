package com.example.platformer.core;

import com.example.platformer.entities.Buff;
import com.example.platformer.entities.Enemy;
import com.example.platformer.entities.Goal;
import com.example.platformer.entities.Player;
import com.example.platformer.generators.BuffGenerator;
import com.example.platformer.generators.EnemyGenerator;
import com.example.platformer.highscores.HighScore;
import com.example.platformer.ui.EndScreen;
import com.example.platformer.ui.PauseScreen;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.input.KeyCode;
import com.example.platformer.map.Map;
import com.example.platformer.map.Platform;
import com.example.platformer.ui.BuffSidebar;
import javafx.stage.Stage;

import java.util.*;

public class GameController extends GameLoop {
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Buff> buffs;
    private com.example.platformer.map.Map map;  // The Map that holds all platforms
    private Pane gameRoot;
    private Scene scene;
    private Goal goal;
    private CollisionManager collisionManager;  // Use CollisionManager for all collision analysis

    private EnemyGenerator enemyGenerator;
    private GameTimer gameTimer;
    private BuffSidebar buffSidebar;

    private GameEndListener gameEndListener;
    private ImageView backgroundView;

    private final int scalingFactor;
    private int playerSpeed;
    private boolean paused;
    private PauseScreen pauseScreen;

    private Scene endScreen;

    public GameController(Pane root, Scene scene, ImageView backgroundImage, BuffSidebar buffSidebar, int scalingFactor, Stage primaryStage, boolean isFullscreen) {
        this.gameRoot = root;
        this.scene = scene;
        this.backgroundView = backgroundImage;
        this.buffSidebar = buffSidebar;
        this.scalingFactor = scalingFactor;
        this.collisionManager = new CollisionManager(buffSidebar.getSideBarWidth(), scene.getWidth());
        this.enemies = new ArrayList<>();
        this.buffs = new ArrayList<>();
        this.goal = new Goal(100, 100);
        this.paused = false;
        this.pauseScreen = new PauseScreen(scene.getWidth(), scene.getHeight(), scene, primaryStage, isFullscreen);
        pauseScreen.getRestartButton().setOnAction(event -> restartGame());

        setupInputHandling(scene);  // Setup keyboard input handling

        gameRoot.getChildren().add(pauseScreen);
    }

    public void startGame() {

        // get screen width and height fromt the game root
        int screenWidth = (int) scene.getWidth();
        int screenHeight = (int) scene.getHeight();
        System.out.println("screenWidth: " + screenWidth + " screenHeight: " + screenHeight);
        map = new Map(gameRoot, screenWidth, screenHeight, buffSidebar.getSideBarWidth(), scalingFactor);

        this.playerSpeed = (int) (screenWidth/3);

        // Generate buffs
        BuffGenerator buffGenerator = new BuffGenerator(map, 0.65, scalingFactor);
        buffs = buffGenerator.generateEntities();
        for (Buff buff : buffs) {
            gameRoot.getChildren().add(buff.getView());
            gameRoot.getChildren().add(buff.getSpriteView());
        }

        //generate goal on the last platform
        this.goal.generateGoal(map.getPlatforms().get(map.getPlatforms().size() - 1));
        gameRoot.getChildren().add(goal.getView());
        gameRoot.getChildren().add(goal.getSpriteView());

        this.gameTimer = new GameTimer(screenWidth, buffSidebar.getSideBarWidth());
        gameRoot.getChildren().add(gameTimer.getTimerText());

        // initialize player lightly above the first platform
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Platform groundPlatform = map.getGroundPlatform();
        player = new Player(groundPlatform.getX() + screenWidth/2,
                groundPlatform.getY() - groundPlatform.getHeight()*scalingFactor,
                playerSpeed, scalingFactor);
        gameRoot.getChildren().add(player.getView());
        gameRoot.getChildren().add(player.getSpriteView());



        // Generate enemies
        enemyGenerator = new EnemyGenerator(map, 0.24, player, playerSpeed/2, scalingFactor);
        enemies = enemyGenerator.generateEntities();
        for (Enemy enemy : enemies) {
            gameRoot.getChildren().add(enemy.getView());
            gameRoot.getChildren().add(enemy.getSpriteView());
        }


        // Start the game loop
        start();
    }

    @Override
    protected void update(double deltaTime) {
        // skip updating if the game is paused
        if (paused) {
            return;
        }
        // Update player and enemies
        player.update(deltaTime);

        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
        }

        for (Buff buff : buffs) {
            buff.update(deltaTime);
        }

        // Handle collisions between player, enemies, and platforms
        handleCollisions();

        if (buffSidebar != null) {
            for (Buff buff : player.getActiveBuffs()) {
                buffSidebar.updateBuff(buff);
            }
            buffSidebar.removeBuffs(new HashSet<>(player.getActiveBuffs()));
        }

        // Follow the player's vertical position with the camera
        if (player.getY() < gameRoot.getHeight() / 2) {
            followPlayer();
        }

       // keeping the timer in the top right corner
        double timerY = -gameRoot.getTranslateY() + 30;
        gameTimer.update(deltaTime, timerY);
        backgroundView.setTranslateY(-gameRoot.getTranslateY());
        pauseScreen.setTranslateY(-gameRoot.getTranslateY());
    }

    private void handleCollisions() {
        // Get platforms from the com.example.platformer.map and check for collisions
        List<Platform> platforms = map.getPlatforms();

        // Check collisions for player
        for (Platform platform : platforms) {
            collisionManager.analyzeEntityCollisions(player, platform);
        }

        Enemy enemyToRemove = null;
        // Check collisions for each enemy
        for (Enemy enemy : enemies) {
            for (Platform platform : platforms) {
                collisionManager.analyzeEntityCollisions(enemy, platform);
            }
            if (!player.isInvincible()) {
                if (collisionManager.areEntitiesColliding(player, enemy)) {
                    player.enemyCollision();
                    enemyToRemove = enemy;
                    // delete the enemy
                    gameRoot.getChildren().remove(enemy.getView());
                    gameRoot.getChildren().remove(enemy.getSpriteView());
                    buffSidebar.enemyCollision(player);
                }
            }
        }
        enemies.remove(enemyToRemove);

        Iterator<Buff> iterator = buffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = iterator.next();
            for (Platform platform : platforms) {
                collisionManager.analyzeEntityCollisions(buff, platform);
            }
            if (collisionManager.areEntitiesColliding(player, buff)) {
                player.applyBuff(buff);
                gameRoot.getChildren().remove(buff.getSpriteView());
                iterator.remove();
                buffSidebar.addBuff(buff);
            }

        }

        if (collisionManager.isEntityCollidingWalls(player)) {
            player.handleWallCollision();
        }

        if (collisionManager.areEntitiesColliding(player, goal)) {
            endGame();
        }
    }

    // Handle keyboard input for player movement and jumping
    private void setupInputHandling(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePause();
                event.consume();
            }

            if (!paused && player.hasCollisionCooldown()) {
                if (event.getCode() == KeyCode.LEFT) {
                    player.moveLeft();
                } else if (event.getCode() == KeyCode.RIGHT) {
                    player.moveRight();
                } else if (event.getCode() == KeyCode.SPACE) {
                    player.jump();
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            if (!paused && (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT)) {
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

    private void togglePause() {
        paused = !paused;
        pauseScreen.setVisible(paused);
        if (paused) {
            pauseScreen.updateTimer(gameTimer.getElapsedTime());
            pauseScreen.toFront();
            System.out.println("Game paused");
        } else {
            System.out.println("Game resumed");
        }
    }


    public void setGameEndListener(GameEndListener listener) {
        this.gameEndListener = listener;
    }

    private void endGame() {
        double time = gameTimer.getElapsedTime();
        System.out.println("You win with time " + time);

        // Create the EndScreen and show it
        EndScreen endScreen = new EndScreen(time);
        Scene endScene = endScreen.createEndScreenScene();

        // Create a new stage for the end screen
        Stage endStage = new Stage();
        endStage.setTitle("Game Over");
        endStage.setScene(endScene);
        endStage.show();

        // Wait for the end screen to be submitted
        endStage.setOnHiding(event -> {
            // Once the window is closed (after the user clicked submit), fetch the username
            String username = endScreen.getUsername(); // Get the username entered by the player

            // Now you can pass this username to the HighScore listener
            if (gameEndListener != null) {
                // Pass the username and time to the gameEndListener
                gameEndListener.onGameEnd(new HighScore(username, new Date(), time));
            }
            // turn off the game

        });

        // Stop the timer and the game loop
        gameTimer.stop();
        stop();
    }

    private void restartGame() {
        gameRoot.getChildren().clear(); // Clear all children
        gameRoot.getChildren().add(backgroundView); // Re-add the background
        gameRoot.getChildren().add(pauseScreen); // Re-add the pause screen
        pauseScreen.setVisible(false); // Ensure the pause screen is hidden initially
        gameRoot.setTranslateY(0); // Reset camera position
        paused = false; // Ensure the game is unpaused
        startGame(); // Reinitialize the game

    }

}

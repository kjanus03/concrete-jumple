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
    private static final int GOAL_WIDTH = 100, GOAL_HEIGHT = 100;
    private static final double BUFF_DENSITY = 0.65;
    private static final double ENEMY_DENSITY = 0.25;

    private static final double SPEED_TO_WIDTH_RATIO = 0.35;
    private static final int TIMER_OFFSET_Y = 30;
    private static final int TIMER_OFFSET_X = 100;
    private final Pane gameRoot;
    private final Scene scene;
    private final Goal goal;
    private final CollisionManager collisionManager;
    private final BuffSidebar buffSidebar;
    private final ImageView backgroundView;
    private final int scalingFactor;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Buff> buffs;
    private com.example.platformer.map.Map map;
    private GameTimer gameTimer;
    private GameEndListener gameEndListener;
    private boolean paused;
    private PauseScreen pauseScreen;


    public GameController(Pane root, Scene scene, ImageView backgroundImage, BuffSidebar buffSidebar, int scalingFactor) {
        this.gameRoot = root;
        this.scene = scene;
        this.backgroundView = backgroundImage;
        this.buffSidebar = buffSidebar;
        this.scalingFactor = scalingFactor;
        this.collisionManager = new CollisionManager(buffSidebar.getSideBarWidth(), scene.getWidth());
        this.enemies = new ArrayList<>();
        this.buffs = new ArrayList<>();
        this.goal = new Goal(GOAL_WIDTH, GOAL_HEIGHT);
        this.paused = false;
        setupInputHandling(scene);  // Setup keyboard input handling
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/game-styles.css")).toExternalForm());


    }

    public void startGame() {
        initializeMap();
        initializeBuffs();
        initializeGoal();
        initializeTimer();
        initializePlayer();
        initializeEnemies();
        start();
    }

    private void initializeMap() {
        map = new Map(gameRoot, (int) scene.getWidth(), (int) scene.getHeight(), buffSidebar.getSideBarWidth(), scalingFactor);
    }

    private void initializePlayer() {
        Platform groundPlatform = map.getGroundPlatform();
        int playerSpeed = (int) (scene.getWidth() * SPEED_TO_WIDTH_RATIO);
        player = new Player(groundPlatform.getX() + scene.getWidth() / 2.0,
                groundPlatform.getY() - groundPlatform.getHeight() * scalingFactor,
                playerSpeed,
                scalingFactor);
        gameRoot.getChildren().addAll(player.getView(), player.getSpriteView());
    }

    private void initializeBuffs() {
        BuffGenerator buffGenerator = new BuffGenerator(map, BUFF_DENSITY, scalingFactor);
        buffs = buffGenerator.generateEntities();
        for (Buff buff : buffs) {
            gameRoot.getChildren().addAll(buff.getView(), buff.getSpriteView());
        }
    }

    private void initializeEnemies() {
        int playerSpeed = (int) (scene.getWidth() * SPEED_TO_WIDTH_RATIO);
        EnemyGenerator enemyGenerator = new EnemyGenerator(map, ENEMY_DENSITY, player, playerSpeed / 2, scalingFactor);
        enemies = enemyGenerator.generateEntities();
        for (Enemy enemy : enemies) {
            gameRoot.getChildren().addAll(enemy.getView(), enemy.getSpriteView());
        }
    }

    private void initializeGoal() {
        goal.generateGoal(map.getPlatforms().get(map.getPlatforms().size() - 1));
        gameRoot.getChildren().addAll(goal.getView(), goal.getSpriteView());
    }

    private void initializeTimer() {
        gameTimer = new GameTimer((int) scene.getWidth(), buffSidebar.getSideBarWidth(), TIMER_OFFSET_X, TIMER_OFFSET_Y);
        gameRoot.getChildren().add(gameTimer.getTimerText());
    }


    @Override
    protected void update(double deltaTime) {
        // skip updating if the game is paused
        if (paused) {
            return;
        }
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

        if (player.getY() < gameRoot.getHeight() / 2) {
            followPlayer();
        }

        // keeping the timer in the top right corner
        double timerY = -gameRoot.getTranslateY() + TIMER_OFFSET_Y;
        gameTimer.update(deltaTime, timerY);
        backgroundView.setTranslateY(-gameRoot.getTranslateY());
        pauseScreen.setTranslateY(-gameRoot.getTranslateY());
    }

    private void handleCollisions() {
        handlePlatformCollisions();
        handleEnemyCollisions();
        handleBuffCollisions();
        handleGoalCollision();
        handleWallCollisions();
    }

    private void handlePlatformCollisions() {
        for (Platform platform : map.getPlatforms()) {
            collisionManager.analyzeEntityCollisions(player, platform);
        }
    }

    private void handleEnemyCollisions() {
        Enemy enemyToRemove = null;
        for (Enemy enemy : enemies) {
            for (Platform platform : map.getPlatforms()) {
                collisionManager.analyzeEntityCollisions(enemy, platform);
            }
            if (!player.isInvincible() && collisionManager.areEntitiesColliding(player, enemy)) {
                player.enemyCollision();
                enemyToRemove = enemy;
                gameRoot.getChildren().remove(enemy.getView());
                gameRoot.getChildren().remove(enemy.getSpriteView());
                buffSidebar.enemyCollision(player);
            }
        }
        enemies.remove(enemyToRemove);

    }

    private void handleBuffCollisions() {
        Iterator<Buff> iterator = buffs.iterator();
        while (iterator.hasNext()) {
            Buff buff = iterator.next();
            for (Platform platform : map.getPlatforms()) {
                collisionManager.analyzeEntityCollisions(buff, platform);
            }
            if (collisionManager.areEntitiesColliding(player, buff)) {
                player.applyBuff(buff);
                gameRoot.getChildren().removeAll(buff.getView(), buff.getSpriteView());
                iterator.remove();
                buffSidebar.addBuff(buff);
            }
        }
    }

    private void handleGoalCollision() {
        if (collisionManager.areEntitiesColliding(player, goal)) {
            endGame();
        }
    }

    private void handleWallCollisions() {
        if (collisionManager.isEntityCollidingWithWall(player)) {
            player.handleWallCollision();
        }
    }

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
        }
    }


    public void setGameEndListener(GameEndListener listener) {
        this.gameEndListener = listener;
    }

    public void setGameAbortListener(GameAbortListener listener, Stage primaryStage, boolean isFullscreen) {
        createPauseScreen(primaryStage, isFullscreen, listener);
    }

    private void createPauseScreen(Stage primaryStage, boolean isFullscreen, GameAbortListener listener) {
        this.pauseScreen = new PauseScreen(scene.getWidth(), scene.getHeight(), scene, primaryStage, isFullscreen, listener);
        pauseScreen.getRestartButton().setOnAction(event -> restartGame());
        gameRoot.getChildren().add(pauseScreen);
    }

    private void endGame() {
        double time = gameTimer.getElapsedTime();

        EndScreen endScreen = new EndScreen(time);
        Scene endScene = endScreen.createEndScreenScene();

        Stage endStage = new Stage();
        endStage.setTitle("Game Over");
        endStage.setScene(endScene);
        endStage.show();

        endStage.setOnHiding(event -> {
            String username = endScreen.getUsername(); // Get the username entered by the player

            if (gameEndListener != null) {
                gameEndListener.onGameEnd(new HighScore(username, new Date(), time));
            }

        });

        gameTimer.stop();
        stop();
    }

    private void restartGame() {
        gameRoot.getChildren().clear();
        gameRoot.getChildren().add(backgroundView);
        gameRoot.getChildren().add(pauseScreen);
        pauseScreen.setVisible(false);
        gameRoot.setTranslateY(0);
        paused = false;
        startGame();
    }
}

package com.example.platformer;

import java.util.ArrayList;

public class EnemyGenerator {
    private Map map;
    private double enemyDensity;
    private Player player;


    public EnemyGenerator(Map map, double enemyDensity, Player player) {
        this.map = map;
        this.enemyDensity = enemyDensity;
        this.player = player;
    }

    public ArrayList<Enemy> generateEnemies() {
        ArrayList<Enemy> enemies = new ArrayList<>();
        for (Platform platform : map.getPlatforms()) {
            if (Math.random() < enemyDensity) {
                Enemy enemy = new Enemy(
                        platform.getView().getTranslateX(),
                        platform.getView().getTranslateY() - 50,
                        player
                );
                enemies.add(enemy);
            }
        }
        return enemies;
    }
}

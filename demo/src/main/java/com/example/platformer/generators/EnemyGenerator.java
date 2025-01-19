package com.example.platformer.generators;

import com.example.platformer.entities.Enemy;
import com.example.platformer.map.Map;
import com.example.platformer.map.Platform;
import com.example.platformer.entities.Player;

public class EnemyGenerator extends AbstractEntityGenerator<Enemy> {
    private Player player;
    private int speed;

    public EnemyGenerator(Map map, double enemyDensity, Player player, int speed, int scalingFactor) {
        super(map, enemyDensity, scalingFactor);
        this.player = player;
        this.speed = speed;
    }

    @Override
    protected Enemy createEntity(double x, double y, Platform platform, int scalingFactor) {
        Enemy enemy = new Enemy(x, y, player, speed, scalingFactor);
        enemy.setSpeed(speed);
        return enemy;
    }
}

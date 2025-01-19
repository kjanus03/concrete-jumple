package com.example.platformer.generators;

import com.example.platformer.entities.Enemy;
import com.example.platformer.map.Map;
import com.example.platformer.map.Platform;
import com.example.platformer.entities.Player;

public class EnemyGenerator extends AbstractEntityGenerator<Enemy> {
    private Player player;

    public EnemyGenerator(Map map, double enemyDensity, Player player, int scalingFactor) {
        super(map, enemyDensity, scalingFactor);
        this.player = player;
    }

    @Override
    protected Enemy createEntity(double x, double y, Platform platform, int scalingFactor) {
        return new Enemy(x, y, player, scalingFactor);
    }
}

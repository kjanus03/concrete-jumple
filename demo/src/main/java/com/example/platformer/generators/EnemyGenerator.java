package generators;

import entities.Enemy;
import map.Map;
import map.Platform;
import entities.Player;

public class EnemyGenerator extends AbstractEntityGenerator<Enemy> {
    private Player player;

    public EnemyGenerator(Map map, double enemyDensity, Player player) {
        super(map, enemyDensity);
        this.player = player;
    }

    @Override
    protected Enemy createEntity(double x, double y, Platform platform) {
        return new Enemy(x, y, player);
    }
}

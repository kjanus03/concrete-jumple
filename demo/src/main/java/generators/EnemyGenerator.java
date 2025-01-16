package generators;

import com.example.platformer.Enemy;
import com.example.platformer.Map;
import com.example.platformer.Platform;
import com.example.platformer.Player;

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

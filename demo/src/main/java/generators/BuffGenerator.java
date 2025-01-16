package generators;

import com.example.platformer.Buff;
import com.example.platformer.BuffType;
import com.example.platformer.Map;
import com.example.platformer.Platform;

public class BuffGenerator extends AbstractEntityGenerator<Buff> {

    public BuffGenerator(Map map, double buffDensity) {
        super(map, buffDensity);
    }

    @Override
    protected Buff createEntity(double x, double y, Platform platform) {
        BuffType type = BuffType.values()[(int) (Math.random() * BuffType.values().length)];
        int buffAmount = (int) (Math.random() * 500);
        return new Buff(x, y, buffAmount, 2, type);
    }
}

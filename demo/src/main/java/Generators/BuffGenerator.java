package Generators;

import com.example.platformer.Buff;
import com.example.platformer.Map;
import com.example.platformer.Platform;

public class BuffGenerator extends AbstractEntityGenerator<Buff> {

    public BuffGenerator(Map map, double buffDensity) {
        super(map, buffDensity);
    }

    @Override
    protected Buff createEntity(double x, double y, Platform platform) {
        int jumpBuff = (int) (Math.random() * 500);
        return new Buff(x, y, jumpBuff, 2);
    }
}

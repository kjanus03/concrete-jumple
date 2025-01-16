package generators;

import com.example.platformer.Entity;
import com.example.platformer.Map;
import com.example.platformer.Platform;

import java.util.ArrayList;

public abstract class AbstractEntityGenerator<T extends Entity> {
    protected Map map;
    protected double entityDensity;

    public AbstractEntityGenerator(Map map, double entityDensity) {
        this.map = map;
        this.entityDensity = entityDensity;
    }

    protected abstract T createEntity(double x, double y, Platform platform);

    public ArrayList<T> generateEntities() {
        ArrayList<T> entities = new ArrayList<>();
        for (Platform platform : map.getPlatforms()) {
            if (Math.random() < entityDensity) {
                T entity = createEntity(
                        platform.getView().getTranslateX(),
                        platform.getView().getTranslateY() - 50,
                        platform
                );
                entities.add(entity);
            }
        }
        return entities;
    }
}

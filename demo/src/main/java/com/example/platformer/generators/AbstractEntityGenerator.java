package com.example.platformer.generators;

import com.example.platformer.entities.Entity;
import com.example.platformer.map.Map;
import com.example.platformer.map.Platform;

import java.util.ArrayList;


public abstract class AbstractEntityGenerator<T extends Entity> {
    private final int scalingFactor;
    protected Map map;
    protected double entityDensity;


    public AbstractEntityGenerator(Map map, double entityDensity, int scalingFactor) {
        this.map = map;
        this.entityDensity = entityDensity;
        this.scalingFactor = scalingFactor;
    }

    protected abstract T createEntity(double x, double y, Platform platform, int scalingFactor);


    public ArrayList<T> generateEntities() {
        ArrayList<T> entities = new ArrayList<>();
        for (Platform platform : map.getPlatforms()) {
            if (Math.random() < entityDensity) {
                double baseX = platform.getView().getTranslateX();
                double baseY = platform.getView().getTranslateY() - 50;

                // generate a random x-offset for the entity
                double xOffset = Math.random() * platform.getWidth();
                double newX = baseX + xOffset;

                T entity = createEntity(newX, baseY, platform, scalingFactor);
                entities.add(entity);
            }
        }
        return entities;
    }

}

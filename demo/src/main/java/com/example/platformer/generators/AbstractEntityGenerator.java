package com.example.platformer.generators;

import com.example.platformer.entities.Entity;
import com.example.platformer.map.Map;
import com.example.platformer.map.Platform;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractEntityGenerator<T extends Entity> {
    protected Map map;
    protected double entityDensity;
    private int scalingFactor;

    // Tracks the x-offset for com.example.platformer.entities generated on each platform
    private final java.util.Map<Platform, Double> platformOffsets = new HashMap<>();

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

                // Adjust x-position to avoid overlap
                double xOffset = platformOffsets.getOrDefault(platform, 0.0);
                double newX = baseX + xOffset;
                platformOffsets.put(platform, xOffset + 40); // Increment offset by 40px

                T entity = createEntity(newX, baseY, platform, scalingFactor);
                entities.add(entity);
            }
        }
        return entities;
    }

}

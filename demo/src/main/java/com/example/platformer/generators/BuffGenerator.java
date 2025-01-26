package com.example.platformer.generators;

import com.example.platformer.entities.Buff;
import com.example.platformer.entities.BuffType;
import com.example.platformer.map.Map;
import com.example.platformer.map.Platform;

public class BuffGenerator extends AbstractEntityGenerator<Buff> {


    public BuffGenerator(Map map, double buffDensity, int scalingFactor) {
        super(map, buffDensity, scalingFactor);
    }

    @Override
    protected Buff createEntity(double x, double y, Platform platform, int scalingFactor) {
        BuffType type = BuffType.values()[(int) (Math.random() * BuffType.values().length)];
        int buffAmount = (int) (Math.random() * 500);
        return new Buff(x, y, buffAmount, type, scalingFactor);
    }
}

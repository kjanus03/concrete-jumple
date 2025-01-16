package com.example.platformer;

import generators.PlatformGenerator;
import javafx.scene.layout.Pane;

import java.util.List;

public class Map {
    private List<Platform> platforms;
    private Pane gameRoot;
    private PlatformGenerator platformGenerator;

    public Map(Pane root) {
        this.gameRoot = root;
        this.platformGenerator = new PlatformGenerator();
        this.platforms = platformGenerator.generatePlatforms();
        addPlatformsToGameRoot();
    }


    private void addPlatformsToGameRoot() {
        for (Platform platform : platforms) {
            gameRoot.getChildren().add(platform.getView());
        }
        System.out.println("Map created with " + platforms.size() + " platforms");
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public Platform getFirstPlatform() {
        return platforms.get(1);
    }

    public String toString() {
        return "Map with " + platforms.size() + " platforms";
    }
}

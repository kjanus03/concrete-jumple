package com.example.platformer;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private List<Platform> platforms;
    private Pane gameRoot;

    public Map(Pane root) {
        this.gameRoot = root;
        this.platforms = new ArrayList<>();
        createPlatforms();
    }

    private void createPlatforms() {
        Platform groundPlatform = new Platform(0, 580, 800, 20);  // A wide platform at the bottom

        Platform platform1 = new Platform(100, 400, 200, 20);
        Platform platform2 = new Platform(350, 300, 150, 20);
        Platform platform3 = new Platform(600, 200, 250, 20);

        platforms.add(groundPlatform);
        platforms.add(platform1);
        platforms.add(platform2);
        platforms.add(platform3);

        gameRoot.getChildren().addAll(
                groundPlatform.getView(),
                platform1.getView(),
                platform2.getView(),
                platform3.getView()
        );

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

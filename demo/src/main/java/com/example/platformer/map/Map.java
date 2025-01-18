package com.example.platformer.map;

import com.example.platformer.generators.PlatformGenerator;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.List;

public class Map {
    private List<Platform> platforms;
    private Pane gameRoot;
    private PlatformGenerator platformGenerator;


    public Map(Pane root, int screenWidth, int screenHeight, int sideBarWidth) {
        this.gameRoot = root;
        this.platformGenerator = new PlatformGenerator(screenWidth, screenHeight);
        this.platforms = platformGenerator.generatePlatforms(sideBarWidth);
        addPlatformsToGameRoot();
    }

    private void addPlatformsToGameRoot() {
        for (int i = 0; i < platforms.size(); i++) {
            gameRoot.getChildren().add(platforms.get(i).getView());
            // add text label platforms number to the game root, the label will be displayed on the platform
            Label label = new Label(String.valueOf(i));
            // set the number in the middle of the platform in front of a grey background
            label.setTranslateX(platforms.get(i).getX() + platforms.get(i).getWidth() / 2 - 5);
            label.setTranslateY(platforms.get(i).getY());
            // set the font size to 20
            label.setStyle("-fx-font-size: 20;");
            label.setStyle("-fx-background-color: #cccccc;");
            gameRoot.getChildren().add(label);


        }
        System.out.println("Map created with " + platforms.size() + " platforms");
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public Platform getGroundPlatform() {
        return platforms.get(0);
    }


    public String toString() {
        return "Map with " + platforms.size() + " platforms";
    }
}

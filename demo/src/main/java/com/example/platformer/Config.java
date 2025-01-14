package com.example.platformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class Config {
    private int height;
    private int width;
    private int playerSpeed;

    public Config() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Config config = mapper.readValue(new File("src/main/resources/config.json"), Config.class);
            this.height = config.height;
            this.width = config.width;
            this.playerSpeed = config.playerSpeed;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getPlayerSpeed() {
        return playerSpeed;
    }
}

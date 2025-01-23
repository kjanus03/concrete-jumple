package com.example.platformer.core;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserSettings {
    private static final String CONFIG_FILE = "src/main/resources/config/user_settings.properties";
    private final Properties properties;
    private int[] screenDimension;

    public UserSettings() {
        properties = new Properties();
        loadSettings();
    }

    // Load settings from the properties file
    private void loadSettings() {
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            screenDimension = new int[2];
            screenDimension[0] = screenSize.width;
            screenDimension[1] = screenSize.height;

        } catch (IOException e) {
            System.out.println("Error loading user settings: " + e.getMessage());
            setDefaults(); // Set default values if the file is missing
        }
    }

    // Save settings to the properties file
    public void saveSettings() {
        try (FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
            loadSettings();
        } catch (IOException e) {
            System.out.println("Error saving user settings: " + e.getMessage());
        }
    }

    // Set default settings if config file is missing
    private void setDefaults() {
        properties.setProperty("volume", "0.5");
        properties.setProperty("fullscreen", "false");
        saveSettings();
    }

    // Getters and setters for the settings
    public double getVolume() {
        return Double.parseDouble(properties.getProperty("volume", "0.5"));
    }

    public void setVolume(double volume) {
        properties.setProperty("volume", String.valueOf(volume));
    }

    public boolean isFullscreen() {
        return Boolean.parseBoolean(properties.getProperty("fullscreen", "false"));
    }

    public void setFullscreen(boolean fullscreen) {
        properties.setProperty("fullscreen", String.valueOf(fullscreen));
    }

    public int getWidth() {
        return screenDimension[0];
    }

    // Get height from the resolution string (e.g., "1280x720")
    public int getHeight() {
        return screenDimension[1];
    }

    public int getScalingFactor() {
        return 2;
    }

}

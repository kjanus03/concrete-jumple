package com.example.platformer.generators;

import com.example.platformer.map.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformGenerator {
    private Random random;
    private double platformWidthMin; // Minimum width for platforms
    private double platformWidthMax; // Maximum width for platforms
    private int platformHeight = 10;  // Base height of platforms
    private int maxVerticalGap = 200;  // Maximum vertical gap between platforms
    private int minVerticalGap = 100;  // Minimum vertical gap between platforms
    private int platformCount = 100;  // Number of platforms to generate

    private int screenWidth;
    private int screenHeight;
    private int scalingFactor;

    public PlatformGenerator(int screenWidth, int screenHeight, int scalingFactor) {
        this.random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.scalingFactor = scalingFactor;

        // Scale the platform dimensions
        platformWidthMin = (screenWidth / 14.4) * scalingFactor;
        platformWidthMax = (screenWidth / 5.8) * scalingFactor;
        platformHeight *= scalingFactor;
        maxVerticalGap *= scalingFactor;
        minVerticalGap *= scalingFactor;
    }

    public List<Platform> generatePlatforms(int sideBarWidth) {
        List<Platform> platforms = new ArrayList<>();
        screenWidth -= sideBarWidth;  // Adjust screen width to account for the sidebar

        // Create the ground platform
        Platform groundPlatform = new Platform(0, screenHeight - platformHeight*scalingFactor, screenWidth*scalingFactor, platformHeight*scalingFactor);
        platforms.add(groundPlatform);

        // Generate random platforms
        double lastPlatformY = screenHeight - platformHeight;
        for (int i = 0; i < platformCount; i++) {
            int platformWidth = (int) ((platformWidthMax - platformWidthMin) + platformWidthMin);
            double platformX = random.nextDouble() * (screenWidth - platformWidth);
            double verticalGap = random.nextDouble() * (maxVerticalGap - minVerticalGap) + minVerticalGap;

            double platformY = lastPlatformY - verticalGap;
            Platform platform = new Platform(platformX, platformY, platformWidth, platformHeight);
            platforms.add(platform);

            lastPlatformY = platformY;
        }

        return platforms;
    }
}

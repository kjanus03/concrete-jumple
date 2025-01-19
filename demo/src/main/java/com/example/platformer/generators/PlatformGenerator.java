package com.example.platformer.generators;
import com.example.platformer.map.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformGenerator {
    private Random random;
    private double platformWidthMin; // Minimum width for platforms
    private double platformWidthMax; // Maximum width for platforms
    private double platformHeight = 10;  // Height of platforms
    private double maxVerticalGap = 140;  // Maximum vertical gap between platforms
    private double minVerticalGap = 90;  // Minimum vertical gap between platforms
    private int platformCount = 100;  // Number of platforms to generate

    private int lastGeneratedX = 0;  // X position of the last generated platform\
    private int screenWidth;
    private int screenHeight;

    public PlatformGenerator(int screenWidth, int screenHeight) {
        this.random = new Random();
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        platformWidthMin = screenWidth / 7.2;
        platformWidthMax = screenWidth / 2.9;


    }

    public List<Platform> generatePlatforms(int sideBarWidth) {

        List<Platform> platforms = new ArrayList<>();
        screenWidth -= sideBarWidth;  // Adjust screen width to account for the sidebar

        Platform groundPlatform = new Platform(0, screenHeight - platformHeight, screenWidth, platformHeight);
        platforms.add(groundPlatform);

        // Generate random platforms up to the 100th platform
        double lastPlatformY = screenHeight - platformHeight;  // Start from the ground platform
        for (int i = 0; i < platformCount; i++) {
            double platformWidth = (random.nextDouble() * (platformWidthMax - platformWidthMin) + platformWidthMin) / platformHeight * platformHeight;
            double platformX = random.nextDouble() * (screenWidth - platformWidth);  // Random X position within screen width
            double verticalGap = random.nextDouble() * (maxVerticalGap - minVerticalGap) + minVerticalGap;  // Random vertical gap

            double platformY = lastPlatformY - verticalGap;  // Set the Y position based on previous platform's Y

            Platform platform = new Platform(platformX, platformY, platformWidth, platformHeight);
            platforms.add(platform);

            lastPlatformY = platformY;  // Update the Y position for the next platform
        }

        return platforms;
    }
}

package generators;
import com.example.platformer.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlatformGenerator {
    private Random random;
    private double screenWidth = 800;  // Width of the game window
    private double screenHeight = 600;  // Height of the game window
    private double platformWidthMin = 100;  // Minimum width for platforms
    private double platformWidthMax = 300;  // Maximum width for platforms
    private double platformHeight = 20;  // Height of platforms
    private double maxVerticalGap = 150;  // Maximum vertical gap between platforms
    private double minVerticalGap = 50;  // Minimum vertical gap between platforms
    private int platformCount = 100;  // Number of platforms to generate

    private int lastGeneratedX = 0;  // X position of the last generated platform

    public PlatformGenerator() {
        this.random = new Random();
    }

    public List<Platform> generatePlatforms() {
        List<Platform> platforms = new ArrayList<>();

        Platform groundPlatform = new Platform(0, screenHeight - platformHeight, screenWidth, platformHeight);
        platforms.add(groundPlatform);

        // Generate random platforms up to the 100th platform
        double lastPlatformY = screenHeight - platformHeight;  // Start from the ground platform
        for (int i = 0; i < platformCount; i++) {
            double platformWidth = random.nextDouble() * (platformWidthMax - platformWidthMin) + platformWidthMin;
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

package com.example.platformer.ui;

import com.example.platformer.entities.Buff;
import com.example.platformer.entities.Player;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BuffSidebar {

    private VBox sidebar;
    private Map<Buff, Label> buffLabels; // Map Buff objects to their labels
    private Label stunLabel; // Label for the stun timer
    private AnimationTimer sidebarUpdater; // Timer to update sidebar elements
    private Player player; // Reference to the player instance
    private int sideBarWidth = 140;

    public BuffSidebar(int sideBarWidth) {
        sidebar = new VBox(10); // Vertical layout with spacing
        this.sideBarWidth = sideBarWidth;
        sidebar.setPrefWidth(sideBarWidth); // Set the preferred width of the sidebar
        URL stylesheetUrl = getClass().getResource("/css/sidebar.css");
        if (stylesheetUrl != null) {
            sidebar.getStylesheets().add(stylesheetUrl.toExternalForm());
        } else {
            System.err.println("Stylesheet not found: /css/sidebar.css");
        }
        sidebar.getStyleClass().add("buff-sidebar");

        buffLabels = new HashMap<>();

        // Initialize an animation timer to regularly update sidebar
        sidebarUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateSidebar();
            }
        };
        sidebarUpdater.start();
    }

    public VBox getSidebar() {
        return sidebar;
    }

    public void addBuff(Buff buff) {
        // Create label for the buff
        Label label = new Label();
        label.getStyleClass().add("buff-label");
        buffLabels.put(buff, label);

        // Create sprite for the buff
        ImageView spriteView = buff.getSpriteView();
        spriteView.setFitWidth(28); // Match entity size
        spriteView.setFitHeight(25);
        spriteView.setTranslateX(label.getTranslateX());
        spriteView.setTranslateY(label.getTranslateY());


        // Create a container for both the label and sprite
        VBox buffContainer = new VBox(5); // Vertical layout with spacing
        buffContainer.getStyleClass().add("buff-container");
        buffContainer.setAlignment(Pos.CENTER); // Center align the contents

        // Add the sprite and label to the container
        buffContainer.getChildren().addAll(spriteView, label);

        // Add the container to the sidebar
        sidebar.getChildren().add(buffContainer);

        // Update the label text to show the buff details
        updateBuff(buff);
    }


    public void updateBuff(Buff buff) {
        if (buffLabels.containsKey(buff)) {
            Label label = buffLabels.get(buff);
            label.setText(buff.getType() + " " + String.format("%.1f", buff.getRemainingTime()) + "s");
        }
    }

    public void removeBuffs(HashSet<Buff> activeBuffs) {
        // Create a list to store buffs to be removed
        HashSet<Buff> buffsToRemove = new HashSet<>();

        // Identify buffs to remove
        for (Buff buff : buffLabels.keySet()) {
            if (!activeBuffs.contains(buff)) {
                buffsToRemove.add(buff);
            }
        }

        // Remove identified buffs
        for (Buff buff : buffsToRemove) {
            Label label = buffLabels.get(buff);
            buffLabels.remove(buff);

            // Find and remove the container holding this buff
            sidebar.getChildren().removeIf(node -> {
                if (node instanceof VBox container && container.getChildren().contains(label)) {
                    return true; // Remove the container
                }
                return false; // Keep the container
            });
        }
    }


    public void enemyCollision(Player player) {
        if (stunLabel == null) { // Only add one stun label
            stunLabel = new Label();
            stunLabel.getStyleClass().add("stun-label");
            sidebar.getChildren().add(stunLabel);
        }

        // Update the label to show remaining stun time
        updateStunTimer(player);
    }

    private void updateStunTimer(Player player) {
        this.player = player; // Store the player instance for later use
        if (stunLabel != null) {
            double remainingTime = player.getRemainingCooldownTime();
            if (remainingTime > 0) {
                stunLabel.setText("Stun " + String.format("%.1f", remainingTime) + "s");
            } else {
                // Remove the label when the timer ends
                sidebar.getChildren().remove(stunLabel);
                stunLabel = null;
            }
        }
    }

    private void updateSidebar() {
        // Update all active buffs
        buffLabels.keySet().forEach(this::updateBuff);

        // Update the stun timer if present
        if (stunLabel != null) {
            updateStunTimer(this.player); // Ensure you pass the current player instance
        }
    }

    public int getSideBarWidth() {
        return sideBarWidth;
    }
}

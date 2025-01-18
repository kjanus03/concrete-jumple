package com.example.platformer.ui;

import com.example.platformer.entities.Buff;
import com.example.platformer.entities.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

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
        sidebar.setStyle("-fx-padding: 10; -fx-background-color: #cccccc;"); // Styling
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
        Label label = new Label();
        label.setFont(new Font(14));
        buffLabels.put(buff, label);
        ImageView spriteView = buff.getSpriteView();
        spriteView.setFitWidth(28); // Match entity size
        spriteView.setFitHeight(25);


        sidebar.getChildren().add(label);
        spriteView.setTranslateX(label.getTranslateX());
        spriteView.setTranslateY(label.getTranslateY());
        sidebar.getChildren().add(spriteView);

        updateBuff(buff);
    }

    public void updateBuff(Buff buff) {
        if (buffLabels.containsKey(buff)) {
            Label label = buffLabels.get(buff);
            label.setText(buff.getType() + " " + String.format("%.1f", buff.getRemainingTime()) + "s");
        }
    }

    public void removeBuffs(HashSet<Buff> activeBuffs) {
        // Iterate over the current buffs in the sidebar
        buffLabels.keySet().removeIf(buff -> {
            if (!activeBuffs.contains(buff)) {
                // Remove the corresponding label from the sidebar
                Label label = buffLabels.get(buff);
                ImageView spriteView = buff.getSpriteView();
                sidebar.getChildren().remove(label);
                sidebar.getChildren().remove(spriteView);
                return true; // Remove the buff from the com.example.platformer.map
            }
            return false; // Keep the buff in the com.example.platformer.map
        });
    }

    public void enemyCollision(Player player) {
        if (stunLabel == null) { // Only add one stun label
            stunLabel = new Label();
            stunLabel.setFont(new Font(14));
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

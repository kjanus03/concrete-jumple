package com.example.platformer;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BuffSidebar {

    private VBox sidebar;
    protected Map<Buff, Label> buffLabels; // Map Buff objects to their labels

    public BuffSidebar() {
        sidebar = new VBox(10); // Vertical layout with spacing
        sidebar.setPrefWidth(140); // Set the preferred width of the sidebar
        sidebar.setStyle("-fx-padding: 10; -fx-background-color: #cccccc;"); // Styling
        buffLabels = new HashMap<>();
    }

    public VBox getSidebar() {
        return sidebar;
    }

    public void addBuff(Buff buff) {
        Label label = new Label();
        label.setFont(new Font(14));
        buffLabels.put(buff, label);
        sidebar.getChildren().add(label);
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
                sidebar.getChildren().remove(label);
                return true; // Remove the buff from the map
            }
            return false; // Keep the buff in the map
        });
    }

}

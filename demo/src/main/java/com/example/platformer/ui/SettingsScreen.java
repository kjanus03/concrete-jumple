package com.example.platformer.ui;

import com.example.platformer.core.UserSettings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class SettingsScreen {

    private final StackPane root;
    private final Stage stage;
    private UserSettings userSettings;
    private final MusicPlayer menuPlayer;

    public SettingsScreen(StackPane root, Stage stage, UserSettings userSettings, MusicPlayer menuPlayer) {
        this.root = root;
        this.stage = stage;  // Stage is now passed to handle fullscreen mode changes
        this.userSettings = userSettings;
        this.menuPlayer = menuPlayer;
    }

    public void setRoot() {
        root.getChildren().clear();  // Clear the current root contents

        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();

        // Create UI controls
        Label titleLabel = new Label("Settings");
        titleLabel.getStyleClass().add("settings-title");

        Label volumeLabel = new Label("Volume:");
        volumeLabel.getStyleClass().add("settings-label");

        Slider volumeSlider = new Slider(0, 1, userSettings.getVolume());
        volumeSlider.getStyleClass().add("settings-slider");
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(0.25);
        volumeSlider.setMinorTickCount(4);
        volumeSlider.setSnapToTicks(true);
        volumeSlider.setPrefWidth(screenWidth / 2);
        volumeSlider.setMaxWidth(screenWidth / 2);

        HBox volumeWrapper = new HBox(10, volumeSlider);
        volumeWrapper.setAlignment(Pos.CENTER);

        VBox volumeSection = new VBox(10, volumeLabel, volumeWrapper);
        volumeSection.setAlignment(Pos.CENTER);

        Label fullscreenLabel = new Label("Fullscreen:");
        fullscreenLabel.getStyleClass().add("settings-label");
        ComboBox<String> fullscreenComboBox = new ComboBox<>();
        fullscreenComboBox.getItems().addAll("Yes", "No");
        fullscreenComboBox.setValue(userSettings.isFullscreen() ? "Yes" : "No");

        Button saveButton = new Button("Save Settings");
        saveButton.getStyleClass().add("menu-button");
        saveButton.setOnAction(event -> {
            userSettings.setVolume(volumeSlider.getValue());
            boolean fullscreen = fullscreenComboBox.getValue().equals("Yes");
            userSettings.setFullscreen(fullscreen);
            userSettings.saveSettings();  // Save settings to the properties file

            // Update the stage fullscreen mode based on the saved settings
            stage.setFullScreen(fullscreen);
            if (!fullscreen){
                stage.setMaximized(true);
            }

            reloadScreen();  // Optionally reload settings or apply other changes
        });

        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("menu-button");
        backButton.setOnAction(event -> {
            // Return to MenuScreen
            MenuScreen menuScreen = new MenuScreen(userSettings);
            menuScreen.setRoot(root, stage);  // Pass the root and the stage to MenuScreen to switch back to the menu
        });

        VBox settingsLayout = new VBox(20);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.getChildren().addAll(titleLabel, volumeSection, fullscreenLabel, fullscreenComboBox, saveButton, backButton);

        Rectangle darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));

        root.getChildren().addAll(darkOverlay, settingsLayout);
    }

    private void reloadScreen() {
        userSettings = new UserSettings();  // Reload settings if necessary

    }
}

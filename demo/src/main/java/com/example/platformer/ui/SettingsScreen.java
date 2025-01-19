package com.example.platformer.ui;

import com.example.platformer.core.UserSettings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SettingsScreen {

    private Stage stage;
    private final Scene previousScene;
    private final UserSettings userSettings;

    public SettingsScreen(Stage stage, Scene previousScene, UserSettings userSettings) {
        this.stage = stage;
        this.previousScene = previousScene;
        this.userSettings = new UserSettings();
    }

    public void show() {
        // Create UI controls
        int screenWidth = userSettings.getWidth();
        int screenHeight = userSettings.getHeight();
        System.out.println("Screen width: " + screenWidth);
        System.out.println("Screen height: " + screenHeight);

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

        // Set the slider to take up half the screen width
        volumeSlider.setPrefWidth(screenWidth / 2);
        volumeSlider.setMaxWidth(screenWidth / 2);

        // Wrap the slider in an HBox to center it
        HBox volumeWrapper = new HBox(10, volumeSlider);
        volumeWrapper.setAlignment(Pos.CENTER);

        // Combine the label and slider into a VBox
        VBox volumeSection = new VBox(10, volumeLabel, volumeWrapper);
        volumeSection.setAlignment(Pos.CENTER);

        Label resolutionLabel = new Label("Resolution:");
        resolutionLabel.getStyleClass().add("settings-label");

        ComboBox<String> resolutionComboBox = new ComboBox<>();
        resolutionComboBox.getItems().addAll("1280x720", "1920x1080");
        resolutionComboBox.setValue(userSettings.getResolution());

        // Save button
        Button saveButton = new Button("Save Settings");
        saveButton.getStyleClass().add("menu-button");
        saveButton.setOnAction(event -> {
            userSettings.setVolume(volumeSlider.getValue());
            userSettings.setResolution(resolutionComboBox.getValue());
            userSettings.saveSettings(); // Save settings to the properties file
            reloadScreen();
        });

        // Back button to return to the previous screen
        Button backButton = new Button("Back to Menu");
        backButton.getStyleClass().add("menu-button");
        backButton.setOnAction(event -> stage.setScene(previousScene));

        // Layout for settings controls
        VBox settingsLayout = new VBox(20);
        settingsLayout.setAlignment(Pos.CENTER);
        settingsLayout.getChildren().addAll(titleLabel, volumeSection, resolutionLabel, resolutionComboBox, saveButton, backButton);

        // Dark overlay for background
        Rectangle darkOverlay = new Rectangle();
        darkOverlay.setFill(Color.rgb(0, 0, 0, 0.8));

        // Bind the overlay size to the screen size
        darkOverlay.widthProperty().bind(stage.widthProperty());
        darkOverlay.heightProperty().bind(stage.heightProperty());

        // StackPane for overlay and content
        StackPane root = new StackPane();
        root.getChildren().addAll(darkOverlay, settingsLayout);

        // Create the scene and apply the stylesheet
        Scene settingsScene = new Scene(root, screenWidth, screenHeight);
        settingsScene.getStylesheets().add(getClass().getResource("/css/menu.css").toExternalForm());

        // Set the scene on the stage
        stage.setScene(settingsScene);
        stage.centerOnScreen();
    }


    private void reloadScreen() {
        MenuScreen menuScreen = new MenuScreen(userSettings);
        menuScreen.show(stage);
        stage.centerOnScreen();
    }
}

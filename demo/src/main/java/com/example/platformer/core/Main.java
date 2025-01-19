package com.example.platformer.core;

import com.example.platformer.ui.MenuScreen;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.DECORATED);
        MenuScreen menuScreen = new MenuScreen(new UserSettings());
        menuScreen.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

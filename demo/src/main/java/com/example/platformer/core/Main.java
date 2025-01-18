package com.example.platformer.core;

import com.example.platformer.ui.MenuScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        MenuScreen menuScreen = new MenuScreen();
        menuScreen.show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

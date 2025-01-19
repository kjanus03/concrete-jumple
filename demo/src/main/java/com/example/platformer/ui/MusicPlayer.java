package com.example.platformer.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer gamePlayer;
    private MediaPlayer menuPlayer;

    public MusicPlayer() {
    }

    public void setGameMusic(String musicFile) {
        try {
            Media sound = new Media(new java.io.File(musicFile).toURI().toString());
            gamePlayer = new MediaPlayer(sound);
            gamePlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void setMenuMusic(String musicFile) {
        try {
            Media sound = new Media(new java.io.File(musicFile).toURI().toString());
            menuPlayer = new MediaPlayer(sound);
            menuPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void playGame() {
        gamePlayer.play();
    }

    public void playMenu() {
//        menuPlayer.play();
    }

    public void stop() {
        if (gamePlayer != null) {
            gamePlayer.stop();
        }
        if (menuPlayer != null) {
            menuPlayer.stop();
        }
    }

    public MediaPlayer getGamePlayer() {
        return gamePlayer;
    }

    // Method to change the volume
    public void setVolume(double volume) {
        gamePlayer.setVolume(volume);
    }
}

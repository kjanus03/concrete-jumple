package com.example.platformer.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer currentPlayer;
    private String currentMusic;

    public MusicPlayer() {}

    public void setGameMusic(String musicFile) {
        setMusic(musicFile);
    }

    public void setMenuMusic(String musicFile) {
        setMusic(musicFile);
    }

    private void setMusic(String musicFile) {
        if (musicFile.equals(currentMusic)) {
            return;
        }

        stop();

        try {
            Media sound = new Media(new java.io.File(musicFile).toURI().toString());
            currentPlayer = new MediaPlayer(sound);
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            currentMusic = musicFile;
        } catch (Exception e) {
            System.out.println("Error loading music file: " + e.getMessage());
        }
    }

    public void play() {
        if (currentPlayer != null && currentPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            currentPlayer.play();
        }
    }

    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    public boolean isPlaying() {
        return currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public void setVolume(double volume) {
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }
}

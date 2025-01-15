package com.example.platformer;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public MusicPlayer(String musicFile) {
        try {
            Media sound = new Media(new java.io.File(musicFile).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void play() {
        mediaPlayer.play();
    }

}

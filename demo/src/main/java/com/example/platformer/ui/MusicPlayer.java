package com.example.platformer.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicPlayer {
    private MediaPlayer currentPlayer; // Tracks the currently playing MediaPlayer
    private String currentMusic;      // Tracks the current music file to avoid unnecessary restarts

    public MusicPlayer() {
    }

    /**
     * Sets and prepares the game music.
     * @param musicFile Path to the game music file.
     */
    public void setGameMusic(String musicFile) {
        setMusic(musicFile);
    }

    /**
     * Sets and prepares the menu music.
     * @param musicFile Path to the menu music file.
     */
    public void setMenuMusic(String musicFile) {
        setMusic(musicFile);
    }

    /**
     * Common method to set music, ensuring only one MediaPlayer is active at a time.
     * @param musicFile Path to the music file.
     */
    private void setMusic(String musicFile) {
        // Avoid reinitializing if the same music is already playing
        if (musicFile.equals(currentMusic)) {
            return;
        }

        // Stop the current music if it's playing
        stop();

        try {
            Media sound = new Media(new java.io.File(musicFile).toURI().toString());
            currentPlayer = new MediaPlayer(sound);
            currentPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
            currentMusic = musicFile; // Update the currently playing music
        } catch (Exception e) {
            System.out.println("Error loading music file: " + e.getMessage());
        }
    }

    /**
     * Starts playing the current music if it’s not already playing.
     */
    public void play() {
        if (currentPlayer != null && currentPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
            currentPlayer.play();
        }
    }

    /**
     * Stops any music currently playing.
     */
    public void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
        }
    }

    /**
     * Checks if the music is currently playing.
     * @return True if the music is playing, false otherwise.
     */
    public boolean isPlaying() {
        return currentPlayer != null && currentPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Changes the volume of the currently playing music.
     * @param volume The volume level (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }
}

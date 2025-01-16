package com.example.platformer;

import highscores.HighScore;

public interface GameEndListener {
    void onGameEnd(HighScore score);
}

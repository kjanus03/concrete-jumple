package com.example.platformer.core;

import com.example.platformer.highscores.HighScore;

public interface GameEndListener {
    void onGameEnd(HighScore score);
}

package com.example.platformer.highscores;

import java.util.Date;

public class HighScore {
    private String username;
    private Date date;
    private double score;

    public HighScore(String username, Date date, double score) {
        this.username = username;
        this.date = date;
        this.score = score;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public double getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}

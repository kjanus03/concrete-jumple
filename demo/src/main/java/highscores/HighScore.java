package highscores;

import java.util.Date;

public class HighScore {
    private String username;
    private Date date;
    private int score;

    public HighScore(String username, Date date, int score) {
        this.username = username;
        this.date = date;
        this.score = score;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}

package highscores;

import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseManager {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> highScoresCollection;

    public DatabaseManager() {
        // Connect to MongoDB server
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        // Access the 'game' database
        database = mongoClient.getDatabase("game");
        // Access the 'highscores' collection
        highScoresCollection = database.getCollection("highscores");
    }

    public void close() {
        mongoClient.close();
    }

    public void insertHighScore(HighScore highScore) {
        Document doc = new Document("username", highScore.getUsername())
                .append("date", highScore.getDate())
                .append("score", highScore.getScore());
        highScoresCollection.insertOne(doc);
    }

    public List<HighScore> getHighScores() {
        List<HighScore> highScores = new ArrayList<>();

        // Retrieve documents sorted by score in descending order
        FindIterable<Document> docs = highScoresCollection.find().sort(Sorts.descending("score")).limit(10);

        for (Document doc : docs) {
            String username = doc.getString("username");
            Date date = doc.getDate("date");
            int score = doc.getInteger("score");

            highScores.add(new HighScore(username, date, score));
        }

        return highScores;
    }

}

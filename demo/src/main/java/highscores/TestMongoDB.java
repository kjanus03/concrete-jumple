package highscores;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

public class TestMongoDB {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            System.out.println("Connected to MongoDB successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

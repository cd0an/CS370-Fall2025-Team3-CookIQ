package cookiq.db;

import java.io.InputStream;       // Used to read the config file
import java.util.Properties;      // Used to load key-value pairs from config.properties

// MongoDB imports
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    // These exist only once throughout the entire app
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // This method connects to MongoDB and returns the database reference
    public static MongoDatabase getDatabase() {
        if (database == null) {  // Only connect once (singleton pattern)
            try {
                // Load connection info from config.properties
                Properties prop = new Properties();
                InputStream input = MongoDBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("config.properties");

                // Check if file exists
                if (input == null)
                    throw new RuntimeException("config.properties not found in classpath!");

                // Read properties from the file
                prop.load(input);

                // Get the MongoDB URI and database name
                String uri = prop.getProperty("mongodb.uri");
                String dbName = prop.getProperty("mongodb.database");

                // Create MongoDB client and get the database
                mongoClient = MongoClients.create(uri);
                database = mongoClient.getDatabase(dbName);
                System.out.println("Connected to MongoDB: " + dbName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to connect to MongoDB", e);
            }
        }
        return database; // Return the active database reference
    }

    // Used when closing the app — shuts down the connection cleanly
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

    // Method only returns the database URI (for debugging or testing)
    private static String loadConnectionString() {
        try (InputStream input = MongoDBConnection.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null)
                throw new RuntimeException("config.properties not found in classpath!");

            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("mongodb.uri");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }
}

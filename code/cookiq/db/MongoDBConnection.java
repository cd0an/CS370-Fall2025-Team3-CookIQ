package db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.io.InputStream;
import java.util.Properties;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;
    
    // Update these with your MongoDB details
    private static final String CONNECTION_STRING = "mongodb+srv://cookIQ:dPVPMTOyr7Hbd5Gt@cluster0.dyw3vjw.mongodb.net/?retryWrites=true&w=majority";
    private static final String DATABASE_NAME = "CookIQ_db";
    
    public static MongoDatabase getDatabase() {
        if (database == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("Connected to MongoDB!");
        }
        return database;
    }
    
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

    private static String loadConnectionString() {
    try (InputStream input = MongoDBConnection.class
            .getClassLoader()
            .getResourceAsStream("config.properties")) {
        Properties prop = new Properties();
        prop.load(input);
        return prop.getProperty("mongodb.uri");
    } catch (Exception e) {
        throw new RuntimeException("Failed to load config", e);
    }
}
}
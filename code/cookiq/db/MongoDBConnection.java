package cookiq.db;

import java.io.InputStream; //Read files
import java.util.Properties; //Stores key - value pairs from a .properties file

//Connect to MongoDB and get db reference
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    //These exist only ONCE throughout the entire app
    private static MongoClient mongoClient; 
    private static MongoDatabase database;
    
    //Use this method when opening the app
    public static MongoDatabase getDatabase() {
       if (database == null) {
            try {
                //Load connection info from config.properties
                Properties prop = new Properties();
                InputStream input = MongoDBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("config.properties");
                prop.load(input);

                String uri = prop.getProperty("mongodb.uri");         //MongoDB uri
                String dbName = prop.getProperty("mongodb.database"); //Database name

                mongoClient = MongoClients.create(uri);
                database = mongoClient.getDatabase(dbName);
                System.out.println("Connected to MongoDB!");
            } catch (Exception e) {
                throw new RuntimeException("Failed to connect to MongoDB", e);
            }
        }
        return database;
    }
    
    //Used this method when closing the app
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed.");
        }
    }

    //Method only returns the database uri
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
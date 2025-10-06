package cookiq.db;

import com.mongodb.client.MongoDatabase;

public class TestMongoConnection {
    public static void main(String[] args) {
        System.out.println("Testing MongoDB Atlas connection...");
        
        try {
            // Get database connection
            MongoDatabase database = MongoDBConnection.getDatabase();
            
            // Print success message
            System.out.println("✓ Successfully connected to MongoDB Atlas!");
            System.out.println("✓ Database name: " + database.getName());
            
            // List collections (will be empty if new database)
            System.out.println("✓ Collections in database:");
            database.listCollectionNames().forEach(System.out::println);
            
            // Close connection
            MongoDBConnection.close();
            System.out.println("✓ Connection closed successfully!");
            
        } catch (Exception e) {
            System.err.println("✗ Connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
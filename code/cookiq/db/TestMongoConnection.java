package cookiq.db;

import com.mongodb.client.MongoDatabase;

public class TestMongoConnection {
    public static void main(String[] args) {
        System.out.println("Testing MongoDB connection...");
        MongoDatabase db = MongoDBConnection.getDatabase();
        System.out.println("✓ Successfully connected to MongoDB Atlas!");
        System.out.println("✓ Database name: " + db.getName());
        System.out.println("✓ Collections in database:");
        db.listCollectionNames().forEach(System.out::println);
        MongoDBConnection.close();
        System.out.println("✓ Connection closed successfully!");
    }
}

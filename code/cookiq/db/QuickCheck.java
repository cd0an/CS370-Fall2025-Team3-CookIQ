package cookiq.db;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class QuickCheck {
    public static void main(String[] args) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("Recipes");
        
        long count = collection.countDocuments();
        System.out.println("Total documents: " + count);
        
        if (count > 0) {
            Document first = collection.find().first();
            System.out.println("\nFull first document:");
            System.out.println(first.toJson());
            
            System.out.println("\nAll field names:");
            first.keySet().forEach(System.out::println);
        } else {
            System.out.println("Collection is empty!");
        }
        
        MongoDBConnection.close();
    }
}
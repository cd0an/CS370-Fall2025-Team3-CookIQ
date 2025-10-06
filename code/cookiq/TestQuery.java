import db.MongoDBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class TestQuery {
    public static void main(String[] args) {
        MongoDatabase database = MongoDBConnection.getDatabase();
        MongoCollection<Document> collection = database.getCollection("recipes");
        
        System.out.println("=== Testing Queries ===\n");
        
        // Query 1: Get all documents
        System.out.println("1. ALL RECIPES:");
        List<Document> allRecipes = new ArrayList<>();
        collection.find().into(allRecipes);
        System.out.println("Total: " + allRecipes.size());
        allRecipes.forEach(doc -> {
            // Use 'title' instead of 'name'
            String title = doc.getString("title");
            System.out.println("  - " + (title != null ? title : "No title"));
        });
        
        // Query 2: Search by ingredient (using your CSV structure)
        System.out.println("\n2. FIND RECIPES WITH CHICKEN:");
        collection.find(Filters.regex("ingredients", "chicken", "i"))
                  .forEach(doc -> System.out.println("  - " + doc.getString("title")));
        
        // Query 3: Search by title
        System.out.println("\n3. SEARCH FOR 'Cookies' IN TITLE:");
        collection.find(Filters.regex("title", "Cookies", "i"))
                  .forEach(doc -> System.out.println("  - " + doc.getString("title")));
        
        // Query 4: Find recipes with specific ingredient patterns
        System.out.println("\n4. RECIPES WITH BUTTER:");
        collection.find(Filters.regex("ingredients", "butter", "i"))
                  .forEach(doc -> System.out.println("  - " + doc.getString("title")));
        
        // Query 5: Count documents
        System.out.println("\n5. COUNT:");
        long count = collection.countDocuments();
        System.out.println("Total recipes: " + count);
        
        // Query 6: Find one document and show ALL fields
        System.out.println("\n6. FIND ONE (SHOW ALL FIELDS):");
        Document first = collection.find().first();
        if (first != null) {
            System.out.println("Title: " + first.getString("title"));
            System.out.println("Ingredients: " + first.get("ingredients"));
            System.out.println("Directions: " + first.get("directions"));
            System.out.println("Source: " + first.get("source"));
            System.out.println("NER: " + first.get("NER"));
            System.out.println("Link: " + first.get("link"));
            System.out.println("Site: " + first.get("site"));
        }
        
        // Query 7: Print all field names to understand structure
        System.out.println("\n7. DOCUMENT STRUCTURE:");
        if (first != null) {
            System.out.println("Available fields: " + first.keySet());
        }

        // Diagnostic: Print first document structure
        System.out.println("=== CHECKING DATA STRUCTURE ===");
        Document firstDoc = collection.find().first();
        if (firstDoc != null) {
            System.out.println("Available fields: " + firstDoc.keySet());
            System.out.println("Full document: " + firstDoc.toJson());
        } else {
            System.out.println("No documents found in collection!");
        }
        MongoDBConnection.close();
    }    
}
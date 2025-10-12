package cookiq.db;

//Import the two below if it's outside the cookiq/db folder
// import cookiq.db.RecipeRepository;
// import cookiq.db.MongoDBConnection;

import org.bson.Document;
import com.mongodb.client.FindIterable;

public class TestApp {
    public static void main(String[] args) {
        

        // ✅ Insert example
        Document recipe = new Document("name", "Beef Stir Fry")
                .append("cuisine", "Asian")
                .append("calories", 600);
        RecipeRepository.insertDocument("test", recipe);

        // ✅ Query all
        System.out.println("\nAll recipes:");
        FindIterable<Document> docs = RecipeRepository.findAll("test");
        for (Document doc : docs) {
            System.out.println(doc.toJson());
        }

        // ✅ Filter query example
        System.out.println("\nLow calorie recipes:");
        Document filter = new Document("calories", new Document("$lt", 700));
        for (Document doc : RecipeRepository.findGivenFilter("test", filter)) {
            System.out.println(doc.toJson());
        }

        // //Delete
        // Document filter_delete = new Document("name", "Beef Stir Fry");
        // RecipeRepository.delete("test", filter_delete);

        // ✅ Close when done
        MongoDBConnection.close();
    }
}

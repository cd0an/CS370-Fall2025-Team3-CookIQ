/**
 * RecipeRepository.java
 *
 * Reads the loaded recipes from the MongoDB dataset and converts them into Recipe objects.
 */

package cookiq.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

public class RecipeRepository
{
    private static final MongoDatabase db = MongoDBConnection.getDatabase();

    //Insertion
    //1st param --> table name
    //2nd param --> a row to be stored
    public static void insertDocument(String collectionName, Document doc)
    {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(doc);
        System.out.println("Inserted document into " + collectionName + ": " + doc.toJson());
    }

    //Find documents in a collection
    public static FindIterable<Document> findAll(String collectionName)
    {
        MongoCollection<Document> collection = db.getCollection((collectionName));
        return collection.find();
    }

    //Query based on preferences
    public static List<Document> findByFilter(String collectionName, String... filters) {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        Document filterDoc = new Document();

        for (String f : filters) {
            String[] parts = f.split("=", 2);
            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];

                // If comma-separated, treat as $in
                if (value.contains(",")) {
                    filterDoc.append(key, new Document("$in", Arrays.asList(value.split(","))));
                } else {
                    // Regex for partial/case-insensitive match (works for array fields too)
                    filterDoc.append(key, Pattern.compile(Pattern.quote(value), Pattern.CASE_INSENSITIVE));
                }
            }
        }

        // Collect all matching documents into a list
        List<Document> results = new ArrayList<>();
        collection.find(filterDoc).into(results);
        return results;
    }
}
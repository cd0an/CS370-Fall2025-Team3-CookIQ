/**
 * RecipeRepository.java
 *
 * Reads the loaded recipes from the MongoDB dataset and converts them into Recipe objects.
 */

package cookiq.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import cookiq.models.Recipe;

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

    // Retrieve existing preferences or save new ones for a user
    public static Document getUserPreferences(String userId, Document newPreferences) {
        MongoCollection<Document> users = db.getCollection("users");

        // Try to find existing preferences
        Document filter = new Document("userId", userId);
        Document userDoc = users.find(filter).first();

        if (userDoc != null) {
            // Existing user found
            Document existingPrefs = (Document) userDoc.get("preferences");

            // If new preferences were provided, update them
            if (newPreferences != null && !newPreferences.isEmpty()) {
                users.updateOne(filter, new Document("$set", new Document("preferences", newPreferences)));
                System.out.println("Updated preferences for existing user: " + userId);
                return newPreferences;
            }

            // Otherwise, just return existing preferences
            System.out.println("Retrieved existing preferences for user: " + userId);
            return existingPrefs != null ? existingPrefs : new Document();
        }

        // No existing user found — if new preferences provided, create new record
        if (newPreferences != null && !newPreferences.isEmpty()) {
            Document newUser = new Document("userId", userId)
                                    .append("preferences", newPreferences);
            users.insertOne(newUser);
            System.out.println("Created new user with preferences: " + userId);
            return newPreferences;
        }

        // No user and no preferences provided
        System.out.println("No preferences found or provided for new user: " + userId);
        return new Document();
    }

    // Get recipes filtered by user preferences
    public static List<Document> getRecipesByPreferences(Document pref) {
        MongoCollection<Document> recipes = db.getCollection("recipes");
        Document filter = new Document();

        // Example: match on diet type
        if (pref.containsKey("diet")) {
            filter.append("diet", Pattern.compile(
                    Pattern.quote(pref.getString("diet")), Pattern.CASE_INSENSITIVE));
        }

        // Example: match on cuisine
        if (pref.containsKey("cuisine")) {
            filter.append("cuisine", Pattern.compile(
                    Pattern.quote(pref.getString("cuisine")), Pattern.CASE_INSENSITIVE));
        }

        // Example: avoid certain ingredients
        if (pref.containsKey("avoidIngredients")) {
            @SuppressWarnings("unchecked")
            List<String> avoid = (List<String>) pref.get("avoidIngredients");
            if (!avoid.isEmpty()) {
                filter.append("ingredients", new Document("$nin", avoid));
            }
        }

        // Collect all matching recipes
        List<Document> results = new ArrayList<>();
        recipes.find(filter).into(results);
        return results;
    }
    
public List<Recipe> getAllRecipes() {
    List<Recipe> recipes = new ArrayList<>();
    
    try {
        MongoCollection<Document> collection = db.getCollection("recipes");
        
        for (Document doc : collection.find()) {
            Recipe recipe = Recipe.parseRecipe(doc);
            recipes.add(recipe);
        }
        
        System.out.println("Successfully loaded " + recipes.size() + " recipes from MongoDB");
    } catch (Exception e) {
        System.err.println("Error fetching recipes from MongoDB: " + e.getMessage());
        e.printStackTrace();
    }
    
    return recipes;
}

}
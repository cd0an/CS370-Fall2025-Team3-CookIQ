/**
 * UserRepository.java
 * 
 * Handles all user-related operations in MongoDB:
 * - Register new users
 * - Retrieve existing users
 * - Update user data (preferences, liked/disliked recipes)
 * - Authenticate users
 * 
 */

package cookiq.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import cookiq.utils.PasswordUtils;

public class UserRepository {

    // Reference to the 'users' collection in MongoDB
    private final MongoCollection<Document> users;

    public UserRepository() {
        // Get the 'users' collection from the MongoDB database
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.users = db.getCollection("users");
    }

    // Registers a new user if the username does not already exist
    public boolean registerUser(String username, String password) {
        if (username == null || password == null)
            return false;

        username = username.toLowerCase(); // Normalize username to lowercase

        // Check if user already exists 
        if (users.find(eq("username", username)).first() != null) {
            return false; // User already exists
        }

        // Hash the password before storing
        String passwordHash = PasswordUtils.sha256(password);

        // Initialize default preferences as JSON string
        String prefJson = cookiq.utils.PreferencesUtils.toJsonString(new cookiq.models.Preferences());

        // Create new user document
        Document newUser = new Document("username", username)
                .append("passwordHash", passwordHash)
                .append("preferences", prefJson)
                .append("likedRecipes", new ArrayList<String>())
                .append("dislikedRecipes", new ArrayList<String>());

        // Insert the new user into the collection
        users.insertOne(newUser);
        System.out.println("Registered new user: " + username);
        return true;
    }

    // Fetches a user document by username
    public Document getUser(String username) {
        if (username == null)
            return null;
        return users.find(eq("username", username.toLowerCase())).first();
    }

    // Updates an existing user document in MongoDB
    public void updateUser(String username, Document updatedUser) {
        if (username == null || updatedUser == null)
            return;
        users.replaceOne(eq("username", username.toLowerCase()), updatedUser);
        System.out.println("Updated user: " + username);
    }

    // Helper function that fetches the list of liked recipes for a user
    public List<String> getLikedRecipes(String username) {
        Document user = getUser(username);
        if (user == null)
            return new ArrayList<>();
        List<String> liked = user.getList("likedRecipes", String.class);
        return liked != null ? liked : new ArrayList<>();
    }

    // Helper function that fetches the list of disliked recipes for a user
    public List<String> getDislikedRecipes(String username) {
        Document user = getUser(username);
        if (user == null)
            return new ArrayList<>();
        List<String> disliked = user.getList("dislikedRecipes", String.class);
        return disliked != null ? disliked : new ArrayList<>();
    }

    // Authenticates a user by comparing the entered password with the stored hash
    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null)
            return false;

        username = username.toLowerCase();
        Document user = getUser(username);
        if (user == null)
            return false;

        String storedHash = user.getString("passwordHash");
        String enteredHash = cookiq.utils.PasswordUtils.sha256(password);

        // Compare hashes in a timing-safe way
        return cookiq.utils.PasswordUtils.slowEquals(storedHash, enteredHash);
    }
}

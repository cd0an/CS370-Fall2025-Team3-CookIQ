/* Manages user accounts — logging in, registering, updating user data. 
 * Handles direct MongoDB queries. */

package cookiq.db;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

import cookiq.security.PasswordUtils;

public class UserRepository {
    private final MongoCollection<Document> users;

    public UserRepository() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.users = db.getCollection("users");
    }

    // Registers a new user if username does not already exist
    public boolean registerUser(String username, String password) {
        username = username.toLowerCase(); // ensure consistency

        if (users.find(eq("username", username)).first() != null) return false;

        String passwordHash = PasswordUtils.sha256(password);

        Document newUser = new Document("username", username)
                .append("passwordHash", passwordHash)
                .append("preferences", new Document())
                .append("likedRecipes", new ArrayList<String>())
                .append("dislikedRecipes", new ArrayList<String>());

        users.insertOne(newUser);
        System.out.println("Registered new user: " + username);
        return true;
    }

    // Retrieves the entire user document by username
    public Document getUser(String username) {
        if (username == null) return null;
        return users.find(eq("username", username.toLowerCase())).first();
    }

    // Updates the entire user document in MongoDB
    public void updateUser(String username, Document updatedUser) {
        if (username == null || updatedUser == null) return;
        users.replaceOne(eq("username", username.toLowerCase()), updatedUser);
        System.out.println("Updated user: " + username);
    }
}



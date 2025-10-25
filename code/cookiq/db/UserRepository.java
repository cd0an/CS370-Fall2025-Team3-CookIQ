/* Manages user accounts — logging in, registering, updating user data. 
 * Handles direct MongoDB queries. */

package cookiq.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;

import cookiq.security.PasswordUtils;

public class UserRepository {
    private final MongoCollection<Document> users;

    public UserRepository() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.users = db.getCollection("users");
    }

    /**
     * Registers a new user if the username does not already exist.
     * Returns true if successful, false if username already exists.
     */
    public boolean registerUser(String username, String password) {
        // Check if username already exists
        Document existingUser = users.find(eq("username", username)).first();
        if (existingUser != null) {
            System.out.println("Username already exists: " + username);
            return false;
        }

        // Hash the password before storing
        String passwordHash = PasswordUtils.sha256(password);

        // Create the new user document
        Document newUser = new Document("username", username)
                .append("passwordHash", passwordHash)
                .append("preferences", new Document()) // empty preferences object
                .append("likedRecipes", new ArrayList<>())
                .append("dislikedRecipes", new ArrayList<>());

        // Insert into MongoDB
        users.insertOne(newUser);
        System.out.println("User registered successfully: " + username);
        return true;
    }

    /**
     * Retrieves a user document by username.
     */
    public Document getUser(String username) {
        return users.find(eq("username", username)).first();
    }
}

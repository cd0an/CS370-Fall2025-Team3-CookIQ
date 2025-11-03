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

    // Registers a new user if username does not already exist
    public boolean registerUser(String username, String password) {
        Document existingUser = users.find(eq("username", username)).first();
        if (existingUser != null) return false;

        // Hash password before storing
        String passwordHash = PasswordUtils.sha256(password);

        // Create new user document
        Document newUser = new Document("username", username)
                .append("passwordHash", passwordHash)
                .append("preferences", new Document())
                .append("likedRecipes", new ArrayList<String>())
                .append("dislikedRecipes", new ArrayList<String>());

        users.insertOne(newUser);
        System.out.println("Registered new user: " + username);
        return true;
    }

    // Retrieves user document by username
    public Document getUser(String username) {
        return users.find(eq("username", username)).first();
    }

    // Updates user document in MongoDB
    public void updateUser(String username, Document updatedUser) {
        users.replaceOne(eq("username", username), updatedUser);
        System.out.println("Updated user: " + username);
    }
}


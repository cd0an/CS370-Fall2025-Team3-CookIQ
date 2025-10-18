/* Manages user accounts — logging in, registering, updating user data. 
 * Handles direct MongoDB queries. */

package cookiq.db;

import org.bson.Document;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.util.List;

public class UserRepository {
    private final MongoCollection<Document> users;

    // Automatically uses the shared MongoDBConnection class
    public UserRepository() {
        this.users = MongoDBConnection.getDatabase().getCollection("users");
    }

    /** Finds a user by username */
    public Document findUserByUsername(String username) {
        return users.find(Filters.eq("username", username)).first();
    }

    /** Creates a new user */
    public void createUser(Document userDoc) {
        users.insertOne(userDoc);
        System.out.println("User added: " + userDoc.getString("username"));
    }

    /** Updates user preferences */
    public void updateUserPreferences(String username, String preferences) {
        users.updateOne(
                Filters.eq("username", username),
                new Document("$set", new Document("preferences", preferences)));
    }

    /** Updates liked recipes */
    public void updateLikedRecipes(String username, List<String> liked) {
        users.updateOne(
                Filters.eq("username", username),
                new Document("$set", new Document("liked", liked)));
    }
}

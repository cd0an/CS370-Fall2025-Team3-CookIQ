/* Manages user accounts — logging in, registering, updating user data. 
 * Handles direct MongoDB queries. */


package cookiq.db;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class UserRepository {
    private final MongoCollection<Document> users;

    public UserRepository(MongoDatabase database) {
        this.users = database.getCollection("users");
    }

    public Document findUserByUsername(String username) {
        return users.find(new Document("username", username)).first();
    }
}


package cookiq.tests;

import cookiq.services.UserService;
import cookiq.db.UserRepository;
import cookiq.db.MongoDBConnection;
import org.bson.Document;
import java.util.List;

public class TestMongoUserService {
    public static void main(String[] args) {
        System.out.println("=== Testing MongoDB User Service ===");

        try {
            // Force MongoDB connection
            MongoDBConnection.getDatabase();

            UserRepository repo = new UserRepository();
            UserService service = new UserService();

            String username = "miguel";
            String password = "test123";
            String recipeName = "Buffalo Chicken Bowl";

            // Register user if not exists
            Document user = repo.getUser(username);
            if (user == null) {
                System.out.println("No user found, registering new one...");
                repo.registerUser(username, password);
            } else {
                System.out.println("User already exists in MongoDB: " + username);
            }

            // Add liked recipe
            service.addLikedRecipe(username, recipeName);

            // Retrieve liked recipes
            List<String> liked = service.getLikedRecipes(username);
            System.out.println("Liked recipes for " + username + ": " + liked);

            // Optional: remove liked recipe (to test update)
            // service.removeLikedRecipe(username, recipeName);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error during MongoDB test: " + e.getMessage());
        } finally {
            MongoDBConnection.close();
        }

        System.out.println("=== Test Completed ===");
    }
}

/**
 * UserService handles all account operations:
 * - Register new users
 * - Login existing users
 * - Save/load users from a file
 */
package cookiq.services;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import cookiq.db.UserRepository;
import cookiq.security.PasswordUtils;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    // Register user
    public boolean registerUser(String username, String password) {
        return userRepository.registerUser(username, password);
    }

    // Login user (verifies hashed password)
    public boolean loginUser(String username, String password) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        String storedHash = user.getString("passwordHash");
        String enteredHash = PasswordUtils.sha256(password);
        return PasswordUtils.slowEquals(storedHash, enteredHash);
    }

    // Add liked recipe for a user
    public void addLikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            return;
        }

        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        if (likedRecipes == null) likedRecipes = new ArrayList<>();

        if (!likedRecipes.contains(recipeName)) {
            likedRecipes.add(recipeName);
            user.put("likedRecipes", likedRecipes);
            userRepository.updateUser(username, user);
            System.out.println("Added '" + recipeName + "' to " + username + "'s liked recipes.");
        }
    }

    // Retrieve liked recipes for a user
    public List<String> getLikedRecipes(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new ArrayList<>();
        return user.getList("likedRecipes", String.class);
    }

    // Remove liked recipe
    public void removeLikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return;

        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        if (likedRecipes != null && likedRecipes.remove(recipeName)) {
            user.put("likedRecipes", likedRecipes);
            userRepository.updateUser(username, user);
            System.out.println("Removed '" + recipeName + "' from " + username + "'s liked recipes.");
        }
    }
}


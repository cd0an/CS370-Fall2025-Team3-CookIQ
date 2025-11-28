/**
 * UserService.java
 * 
 * UserService handles all account operations:
 * - Register and login users
 * - Save/load user preferences
 * - Manage liked/disliked recipes
 * - Fetch full user objects with preferences 
 * 
 */

package cookiq.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import cookiq.db.RecipeRepository;
import cookiq.db.UserRepository;
import cookiq.models.Preferences;
import cookiq.models.Recipe;
import cookiq.models.User;
import cookiq.utils.PasswordUtils;
import cookiq.utils.PreferencesUtils;

public class UserService {
    private final UserRepository userRepository; // Call the UserRepository for DB operations

    // Singleton pattern
    public UserService() {
        userRepository = new UserRepository();
    }

    // ==================== User Registration/Login ====================

    // Register a new user with username and password 
    public boolean registerUser(String username, String password) {
        return userRepository.registerUser(username, password);
    }

    /** 
     * Attempt to login an existing user. 
     * Compares hashed password with stored hash using secure comparison. 
    */
    public boolean loginUser(String username, String password) {
        if (username == null || password == null) return false;
        username = username.trim().toLowerCase();
        password = password.trim();

        Document user = userRepository.getUser(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            return false;
        }

        String storedHash = user.getString("passwordHash");
        String enteredHash = PasswordUtils.sha256(password);

        System.out.println("Trying login for: " + username);
        System.out.println("Stored hash: " + storedHash);
        System.out.println("Entered hash: " + enteredHash);

        return PasswordUtils.slowEquals(storedHash, enteredHash);
    }

    // ==================== Full User Fetch ====================

    /** 
     * Get a full User object by username 
     * Loads liked/disliked recipes and preferences.
     */
    public User getUserByUsername(String username) {
        Document userDoc = userRepository.getUser(username);
        if (userDoc == null) return null;

        User user = new User(username, ""); // Password not needed for new users 

        List<String> liked = userDoc.getList("likedRecipes", String.class);
        if (liked != null) user.getLikedRecipes().addAll(liked);

        List<String> disliked = userDoc.getList("dislikedRecipes", String.class);
        if (disliked != null) user.getDislikedRecipes().addAll(disliked);

         Object prefObj = userDoc.get("preferences");
        if (prefObj instanceof Document) {
            Document prefDoc = (Document) prefObj;
            user.getPreferences().copyPrefs(PreferencesUtils.fromDocument(prefDoc));
        } else if (prefObj instanceof String) {
            user.getPreferences().copyPrefs(PreferencesUtils.fromJsonString((String) prefObj));
        } else {
            user.setPreferences(new Preferences());
        }

        return user;
    }

    // ==================== Liked Recipes ====================

    // Add a recipe to user's liked list
    public boolean addLikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        if (likedRecipes == null) likedRecipes = new ArrayList<>();

        if (!likedRecipes.contains(recipeName)) {
            likedRecipes.add(recipeName);
            user.put("likedRecipes", likedRecipes);
            userRepository.updateUser(username, user);
            return true;
        }
        return false;
    }

    // Get names of all recipes liked by the user 
    public List<String> getLikedRecipes(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new ArrayList<>();
        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        return likedRecipes != null ? likedRecipes : new ArrayList<>();
    }

    // Remove a recipe from a user's liked list 
    public boolean removeLikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        if (likedRecipes == null) likedRecipes = new ArrayList<>();

        if (likedRecipes.remove(recipeName)) {
            user.put("likedRecipes", likedRecipes);
            userRepository.updateUser(username, user);
            return true;
        }
        return false;
    }

    // Get full Recipe objects for all liked recipes 
    public List<Recipe> getLikedRecipesFull(String username) {
        List<String> likedNames = getLikedRecipes(username);
        List<Recipe> likedRecipes = new ArrayList<>();
        if (likedNames.isEmpty()) return likedRecipes;

        List<Recipe> allRecipes = new RecipeRepository().getAllRecipes();
        for (Recipe r : allRecipes) {
            if (likedNames.contains(r.getName())) likedRecipes.add(r);
        }
        return likedRecipes;
    }

    // ==================== Disliked Recipes ====================

    // Add a recipe to user's disliked list
    public boolean addDislikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        List<String> dislikedRecipes = user.getList("dislikedRecipes", String.class);
        if (dislikedRecipes == null) dislikedRecipes = new ArrayList<>();

        if (!dislikedRecipes.contains(recipeName)) {
            dislikedRecipes.add(recipeName);
            user.put("dislikedRecipes", dislikedRecipes);
            userRepository.updateUser(username, user);
            return true;
        }
        return false;
    }

    // Get names of all recipes disliked by the user
    public List<String> getDislikedRecipes(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new ArrayList<>();
        List<String> dislikedRecipes = user.getList("dislikedRecipes", String.class);
        return dislikedRecipes != null ? dislikedRecipes : new ArrayList<>();
    }

    // ==================== User Preferences ====================

    // Save a user's preferences to the database
    public boolean saveUserPreferences(String username, Preferences prefs) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        String prefStr = PreferencesUtils.toJsonString(prefs);
        user.put("preferences", prefStr);

        userRepository.updateUser(username, user);
        return true;
    }

    // Retrieve a user's preferences from the database
    public Preferences getUserPreferences(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new Preferences();

        Object prefObj = user.get("preferences");
        if (prefObj instanceof Document) {
            return PreferencesUtils.fromDocument((Document) prefObj);
        } else if (prefObj instanceof String) {
            return PreferencesUtils.fromJsonString((String) prefObj);
        }
        return new Preferences();
    }

    // ==================== Utility ====================

    // Print a user's preferences to console for debugging
    public void printPreferences(Preferences prefs) {
        System.out.println("Vegetarian:" + prefs.isVegetarian());
        System.out.println("Keto:" + prefs.isKeto());
        System.out.println("Gluten Free:" + prefs.isGlutenFree());
        System.out.println("Low Calorie:" + prefs.isLowCalorie());
        System.out.println("High Calorie:" + prefs.isHighCalorie());
        System.out.println("High Protein:" + prefs.isHighProtein());
        System.out.println("Italian:" + prefs.isItalian());
        System.out.println("Mexican:" + prefs.isMexican());
        System.out.println("Asian:" + prefs.isAsian());
        System.out.println("American:" + prefs.isAmerican());
        System.out.println("Mediterranean:" + prefs.isMediterranean());
        System.out.println("Max Cook Time:" + prefs.getMaxCookTime());
        System.out.println("Max Budget:" + prefs.getMaxBudget());
        System.out.println("Available Ingredients:" + prefs.getAvailableIngredients());
    }
}

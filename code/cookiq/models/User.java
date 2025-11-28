/**
 * User.java
 *
 * Represents a user of CookIQ, storing username, password, preferences,
 * and history of liked/disliked recipes.
 * 
 */

package cookiq.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username; // Username for login 
    private String password; // Hashed password 
    private Preferences preferences; // User's dietary, health, cuisine, and ingredient preferences
    private List<String> liked; // List of recipe IDs the user has liked
    private List<String> disliked; // List of recipe IDs the user has disliked

    // Constructor for new users
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.preferences = new Preferences();
        this.liked = new ArrayList<>();
        this.disliked = new ArrayList<>();
    }

    // Constructor for loading existing users
    public User(String username, String password, Preferences preferences, List<String> likedRecipes, List<String> dislikedRecipes, List<String> seenRecipes) {
        this.username = username;
        this.password = password;
        this.preferences = preferences != null ? preferences : new Preferences();
        this.liked = likedRecipes != null ? new ArrayList<>(likedRecipes) : new ArrayList<>();
        this.disliked = dislikedRecipes != null ? new ArrayList<>(dislikedRecipes) : new ArrayList<>();
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Preferences getPreferences() { return preferences; }

    // Return copies of liked/disliked recipes to prevent external modification
    public List<String> getLikedRecipes() { return new ArrayList<>(liked); }
    public List<String> getDislikedRecipes() { return new ArrayList<>(disliked);}

    // ======================= Add/Remove Recipes =======================

    // Add a recipe to the liked list if not already present
    public void addLikedRecipe(String recipeId) {
        if (!liked.contains(recipeId)) {
            liked.add(recipeId);
        }
    }

    // Add a recipe to the disliked list if not already present
    public void addDislikedRecipe(String recipeId) {
        if (!disliked.contains(recipeId)) {
            disliked.add(recipeId);
        }
    }

    // Remove a recipe from the liked list
    public void removeLikedRecipe(String recipeId) {
        if (recipeId != null) {
            liked.remove(recipeId);
        }
    }

    // Remove a recipe from the disliked list
    public void removeDislikedRecipe(String recipeId) {
        if (recipeId != null) {
            disliked.remove(recipeId);
        }
    }

    // Set or update user preferences
    public void setPreferences(Preferences preferences) {
        this.preferences = preferences != null ? preferences : new Preferences();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked + 
                '}';
    }
}

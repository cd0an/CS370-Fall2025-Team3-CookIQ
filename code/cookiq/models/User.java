/**
 * User.java
 *
 * Represents a user of CookIQ, storing username, password, preferences,
 * and history of liked/disliked recipes.
 */

package cookiq.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private Preferences preferences;
    private List<String> liked;
    private List<String> disliked;

    // Constructor for new users
    public User(String username, String password, Preferences preferences) {
        this.username = username;
        this.password = password;
        this.preferences = preferences;
        this.liked = new ArrayList<>();
        this.disliked = new ArrayList<>();
    }

    // Constructor for loading users with liked recipes
    public User(String username, String password, Preferences preferences, List<String> likedRecipes) {
        this(username, password, preferences);
        if (likedRecipes != null) this.liked = likedRecipes;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Preferences getPreferences() { return preferences; }
    public List<String> getLikedRecipes() { return liked; }
    public List<String> getDislikedRecipes() { return disliked; }

    // public void addLikedRecipe(String recipe) {
    //     if (!liked.contains(recipe)) {
    //         liked.add(recipe);
    //         disliked.remove(recipe);
    //     }
    // }

    // public void addDislikedRecipe(String recipe) {
    //     if (!disliked.contains(recipe)) {
    //         disliked.add(recipe);
    //         liked.remove(recipe);
    //     }
    // }

    // public void setPreferences(Preferences preferences) {
    //     this.preferences = preferences != null ? preferences : new Preferences();
    // }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked +
                '}';
    }
}

/**
 * User.java
 *
 * Represents a user of CookIQ, storing username, password, preferences,
 * and history of liked/disliked recipes.
 */


package models;

import java.util.ArrayList;
import java.util.List;

public class User { // declares class named user . can be accessed from other classes in program 
    private String username; // username field
    private String password; // password field
    private String preferences; // preferences field
    private List<String> liked; // liked recipes field
    private List<String> disliked; // disliked recipes field

    // Constructor → makes a new User object
    public User(String username, String password, String preferences) {
        this.username = username; // assigns the value of the argumnet username to the objects's username field
        this.password = password;
        this.preferences = preferences;
        this.liked = new ArrayList<>(); // initializes likedRecipes as an empty list
        this.disliked = new ArrayList<>(); // initializes dislikedRecipes as an empty list
    }

    // Getter methods → allow safe access to fields
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPreferences() {
        return preferences;
    }

    public List<String> getLiked() {
        return liked;
    }

    public List<String> getDisliked() {
        return disliked;
    }

    //setter methods → allow safe modification of fields
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

     // Add or remove liked/disliked items
    public void addLiked(String item) {
        if (!liked.contains(item)) {
            liked.add(item);
            disliked.remove(item); // make sure it's not both liked and disliked
        }
    }

    public void addDisliked(String item) {
        if (!disliked.contains(item)) {
            disliked.add(item);
            liked.remove(item);
        }
    }

    public void removeLiked(String item) {
        liked.remove(item);
    }

    public void removeDisliked(String item) {
        disliked.remove(item);
    }

    // For debugging or printing user info
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", preferences='" + preferences + '\'' +
                ", liked=" + liked +
                ", disliked=" + disliked +
                '}';
    }
}



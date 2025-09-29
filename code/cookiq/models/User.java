/**
 * User.java
 *
 * Represents a user of CookIQ, storing username, password, preferences,
 * and history of liked/disliked recipes.
 */


package cookiq.models;

public class User { // declares class named user . can be accessed from other classes in program 
    private String username; // username field
    private String password; // password field

    // Constructor → makes a new User object
    public User(String username, String password) {
        this.username = username; // assigns the value of the argumnet username to the objects's username field
        this.password = password;
    }

    // Getter methods → allow safe access to fields
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

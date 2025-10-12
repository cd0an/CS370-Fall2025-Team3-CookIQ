/**
 * UserService handles all account operations:
 * - Register new users
 * - Login existing users
 * - Save/load users from a file
 */

package cookiq.services;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import cookiq.models.User;

public class UserService {
    private static final String USER_FILE = "data/users.txt"; // File to store accounts
    private List<User> users; // List of all loaded users

    // Constructor → load existing users from file
    public UserService() {
        users = loadUsers();
    }

    /**
     * Registers a new user if the username doesn't already exist.
     * 
     * @return true if successful, false if username taken
     */
    public boolean registerUser(String username, String password) {
        // Check if username already exists
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return false; // User already exists
            }
        }

        // Create new user (empty preferences by default)
        User newUser = new User(username, password, "");
        users.add(newUser);
        saveUsers(); // Save updated list to file
        return true;
    }

    /**
     * Logs in a user if username + password match.
     * 
     * @return the User object if successful, otherwise null
     */
    public User loginUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u; // Found a match
            }
        }
        return null; // Invalid login
    }

    /**
     * Saves all users to the data file.
     */
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                writer.write(u.getUsername() + "," + u.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Loads users from the data file at startup.
     */
    private List<User> loadUsers() {
        List<User> loaded = new ArrayList<>();
        File file = new File(USER_FILE);

        // If file doesn’t exist, return empty list
        if (!file.exists())
            return loaded;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    loaded.add(new User(parts[0], parts[1], ""));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }

        return loaded;
    }
}

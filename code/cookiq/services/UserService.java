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
    private static final String USER_FILE = "../data/users.txt";
    private List<User> users;

    public UserService() {
        users = loadUsers();
    }

    /** Registers a new user if username doesn't already exist */
    public boolean registerUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username)) return false; // username already taken
        }

        users.add(new User(username, password, ""));
        saveUsers();
        return true;
    }

    /** Logs in a user if username + password match */
    public User loginUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null; // no match found
    }

    /** Save users to text file */
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            for (User u : users) {
                writer.write(u.getUsername() + "," + u.getPassword() + "," + u.getPreferences());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /** Load users from text file */
    private List<User> loadUsers() {
        List<User> loaded = new ArrayList<>();
        File file = new File(USER_FILE);
        if (!file.exists()) return loaded;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String username = parts[0];
                    String password = parts[1];
                    String preferences = parts.length > 2 ? parts[2] : "";
                    loaded.add(new User(username, password, preferences));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }

        return loaded;
    }
}

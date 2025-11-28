/**
 * UserSession.java
 * 
 * Manages the current session (logged-in user or guest).
 * Singleton pattern ensures one global session across the app.
 * 
 */

package cookiq.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONObject;

import cookiq.models.User;

public class UserSession {
    private static UserSession instance; // Singleton instance
    private User currentUser; // Currently logged-in user
    private boolean guestMode; // True if in guest mode
    private static final String SESSION_FILE = "session.json"; // Session persistence file

    // Private constructor for Singleton
    private UserSession() {}

    // Get the singleton instance of UserSession
    public static synchronized UserSession getInstance() {
        if (instance == null)
            instance = new UserSession();
        return instance;
    }

    // ==================== User Registration/Login ====================

    // Log in a registered user 
    public void login(User user) {
        this.currentUser = user;
        this.guestMode = false;
        saveSession();
        System.out.println("Session started for: " + user.getUsername());
    }

    // Start a guest session (no user account)
    public void loginAsGuest() {
        this.currentUser = null;
        this.guestMode = true;
        saveSession();
        System.out.println("Guest session started.");
    }

    // Log out completely and clear session 
    public void logout() {
        System.out.println("Session ended for: " + getUsernameOrGuest());
        this.currentUser = null;
        this.guestMode = false;
        clearSavedSession();
    }

    // ==================== Session State ====================

    // Check if a registered user is logged in 
    public boolean isLoggedIn() {
        return currentUser != null && !guestMode;
    }

    // Check if the current session is a guest session 
    public boolean isGuest() {
        return guestMode;
    }

    // Get the current user object (null if guest)
    public User getCurrentUser() {
        return currentUser;
    }

    // Set the current user manually 
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.guestMode = user == null || "Guest".equals(user.getUsername());
    }

    // Return username if logged in, or "Guest" if in guest mode
    public String getUsernameOrGuest() {
        return isLoggedIn() ? currentUser.getUsername() : "Guest";
    }

    // ==================== Session Persistence ====================

    // Save the current session to session.json
    public void saveSession() {
        try (FileWriter writer = new FileWriter(SESSION_FILE)) {
            JSONObject json = new JSONObject();
            if (isLoggedIn()) {
                json.put("username", currentUser.getUsername());
                json.put("guestMode", false);
            } else {
                json.put("username", JSONObject.NULL);
                json.put("guestMode", guestMode);
            }
            writer.write(json.toString(2));
            System.out.println("Session saved: " + json);
        } catch (Exception e) {
            System.err.println("Failed to save session: " + e.getMessage());
        }
    }

    // Load session from session.json
    public void loadSession() {
        File f = new File(SESSION_FILE);
        if (!f.exists())
            return;
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            JSONObject json = new JSONObject(sb.toString());

            if (json.optBoolean("guestMode", false)) {
                loginAsGuest();
                return;
            }

            String username = json.optString("username", null);
            if (username != null && !"null".equals(username)) {
                cookiq.services.UserService service = new cookiq.services.UserService();
                cookiq.models.User user = service.getUserByUsername(username);
                if (user != null)
                    login(user);
            }
        } catch (Exception e) {
            System.err.println("Failed to load session: " + e.getMessage());
        }
    }

    // Delete the saved session file
    public void clearSavedSession() {
        new File(SESSION_FILE).delete();
    }
}

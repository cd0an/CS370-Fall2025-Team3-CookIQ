/**
 * Manages the current session (logged-in user or guest).
 * Singleton pattern ensures one global session across the app.
 */

package cookiq.services;

import cookiq.models.User;
import org.json.JSONObject;
import java.io.*;

public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private boolean guestMode;
    private static final String SESSION_FILE = "session.json";

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
        if (instance == null)
            instance = new UserSession();
        return instance;
    }

    /** Log in a registered user */
    public void login(User user) {
        this.currentUser = user;
        this.guestMode = false;
        saveSession();
        System.out.println("Session started for: " + user.getUsername());
    }

    /** Start guest session */
    public void loginAsGuest() {
        this.currentUser = null;
        this.guestMode = true;
        saveSession();
        System.out.println("Guest session started.");
    }

    /** Log out completely */
    public void logout() {
        System.out.println("Session ended for: " + getUsernameOrGuest());
        this.currentUser = null;
        this.guestMode = false;
        clearSavedSession();
    }

    public boolean isLoggedIn() {
        return currentUser != null && !guestMode;
    }

    public boolean isGuest() {
        return guestMode;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.guestMode = user == null || "Guest".equals(user.getUsername());
    }

    public String getUsernameOrGuest() {
        return isLoggedIn() ? currentUser.getUsername() : "Guest";
    }

    // === Session Persistence ===

    /** Save session to session.json */
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

    /** Load session from session.json */
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

    /** Delete session.json */
    public void clearSavedSession() {
        new File(SESSION_FILE).delete();
    }
}

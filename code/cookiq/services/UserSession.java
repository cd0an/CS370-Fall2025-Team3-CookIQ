/**
 * Manages the current session (logged-in user or guest).
 * Singleton pattern ensures one global session across the app.
 */

package cookiq.services;

import cookiq.models.User;

public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private boolean guestMode;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    /** Log in a registered user */
    public void login(User user) {
        this.currentUser = user;
        this.guestMode = false;
        System.out.println("Session started for: " + user.getUsername());
    }

    /** Start as guest (temporary session) */
    public void loginAsGuest() {
        this.currentUser = null;
        this.guestMode = true;
        System.out.println("Guest session started.");
    }

    /** Log out completely */
    public void logout() {
        System.out.println("Session ended for: " + getUsernameOrGuest());
        this.currentUser = null;
        this.guestMode = false;
    }

    /** Check if a real user is logged in */
    public boolean isLoggedIn() {
        return currentUser != null && !guestMode;
    }

    /** Check if guest mode is active */
    public boolean isGuest() {
        return guestMode;
    }

    /** Get the current user object (null if guest) */
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.guestMode = user == null || "Guest".equals(user.getUsername());
    }

    /** Get username, or “Guest” if not logged in */
    public String getUsernameOrGuest() {
        return isLoggedIn() ? currentUser.getUsername() : "Guest";
    }
}


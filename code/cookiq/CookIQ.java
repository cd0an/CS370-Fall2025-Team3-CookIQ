/**
 * CookIQ.java
 *
 * Main entry point for the CookIQ personalized meal recommendation system.
 * 
 */

package cookiq;

import cookiq.models.User;
import cookiq.services.UserSession;
import cookiq.ui.MainFrame;

public class CookIQ {
    public static void main(String[] args) {
        // Load session (if any) or start guest session 
        UserSession session = UserSession.getInstance();
        session.loadSession(); // Load a saved session if it exists

        User currentUser = session.getCurrentUser();
        if (currentUser == null) {
            // If no user loaded, start as guest 
            session.loginAsGuest();
            currentUser = session.getCurrentUser();
        }

        // Launch main window
        new MainFrame(currentUser);
    }
}

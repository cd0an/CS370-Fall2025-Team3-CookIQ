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
        UserSession session = UserSession.getInstance(); //Singleton session manager
        session.loadSession(); //Load a saved session if it exists

        User currentUser = session.getCurrentUser(); //Retrieve the currently actice user (null if guest or no saved session)

        if (currentUser == null || session.isGuest()) {
            //If no logged-in user, or guest session, show the login UI
            javax.swing.JFrame loginFrame = new javax.swing.JFrame("Login");
            loginFrame.setContentPane(new cookiq.ui.LoginUI()); // Add login panel
            loginFrame.pack(); // Size frame to fit content
            loginFrame.setLocationRelativeTo(null); // Center on screen
            loginFrame.setVisible(true); // Show the login window
        } else {
            //If a user is already logged in, go directly to the main application UI
            new MainFrame(currentUser);
        }
    }
}

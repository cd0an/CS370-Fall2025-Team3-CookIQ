/* Sets up the window, keeps the navbar at the top, and displays the main content panel */

package cookiq.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cookiq.models.Preferences;

public class MainFrame extends JFrame {
    private NavbarPanel navbar; // Top navigation bar 
    private JPanel mainPanel; // Main content panel that show different screens 
    private CardLayout cardLayout; // Main layout manager to switch between screens 
    private PreferencesUI preferencesUI;
    private SwipeUI swipeUI;

    // Constructor 
    public MainFrame() {
        setTitle("CookIQ Recipe Generator");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ======================== Navbar ======================== 
        navbar = new NavbarPanel(new NavListener());
        add(navbar, BorderLayout.NORTH);

        // ======================== Main Panel with CardLayout ======================== 
        cardLayout = new CardLayout(); // Allos switching between panels 
        mainPanel = new JPanel(cardLayout);

        preferencesUI = new PreferencesUI(this);
        swipeUI = new SwipeUI(this);

        mainPanel.add(preferencesUI, "Preferences");
        mainPanel.add(swipeUI, "Swipe");

        add(mainPanel, BorderLayout.CENTER); // Add main panel below navbar

        cardLayout.show(mainPanel, "Preferences"); // Show Preferences screen first 

        setVisible(true);
    }

    // ======================== Method to switch to SwipeUI ========================
    public void showSwipeUI(Preferences prefs) {
        swipeUI.setUserPreferences(prefs);
        cardLayout.show(mainPanel, "Swipe");
    }

    // ======================== Method to switch to ShowPreferencesUI ========================
    public void showPreferencesUI() {
        cardLayout.show(mainPanel, "Preferences");
    }


    // ======================== Navbar Action Listener ========================
    private class NavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            
            if (source == navbar.getHomeBtn()) {
                // cardLayout.show(mainPanel, "Home");
            } else if (source == navbar.getPreferencesBtn()) {
                cardLayout.show(mainPanel, "Preferences");
            } else if (source == navbar.getLikedBtn()) {
                // cardLayout.show(mainPanel, "LikedRecipes");
            } else if (source == navbar.getMealMatchBtn()) {
                cardLayout.show(mainPanel, "Swipe");
            } else if (source == navbar.getLoginBtn()) {
                // Handle Login 
            }
        }
    } 

    // Main method 
    public static void main(String[] args) {
        new MainFrame();
    }
}




/* Sets up the window, keeps the navbar at the top, and displays the main content panel */

package cookiq.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import cookiq.models.Preferences;
import cookiq.models.Recipe;
import cookiq.models.User;
import cookiq.services.RecommendationService;
import cookiq.services.UserService;
import cookiq.services.UserSession;

public class MainFrame extends JFrame {
    private NavbarPanel navbar; // Top navigation bar 
    private JPanel mainPanel; // Main content panel that show different screens 
    private CardLayout cardLayout; // Main layout manager to switch between screens 

    private PreferencesUI preferencesUI;
    private SwipeUI swipeUI;
    private RecipeDetailsUI recipeDetailsUI;
    private LikedRecipeUI likedRecipeUI;
    private HomeDashboardUI homeDashboardUI;
    private RecipeDetailsUI detailsUI;

    private User currentUser; // Currently logged-in 
    private List<String[]> likedRecipesList = new ArrayList<>();

    private final UserService userService; // SINGLE UserService instance
    private final RecommendationService recommendationService; 

    // Constructor 
    public MainFrame(User user) {
        this.currentUser = user; // Set current user
        this.userService = new UserService(); 
        this.recommendationService = new RecommendationService();

        setTitle("CookIQ Recipe Generator");
        setSize(1000,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Make sure session reflects guest or user properly
        if (UserSession.getInstance().getCurrentUser() == null && !UserSession.getInstance().isGuest()) {
            UserSession.getInstance().loginAsGuest(); // Default to guest if no one logged in
        }

        // ======================== Navbar ======================== 
        navbar = new NavbarPanel(new NavListener());
        add(navbar, BorderLayout.NORTH);

        updateNavbarForUser(); // Enable/disable Liked button based on user
        navbar.updateLoginStatus(); 

        // ======================== Main Panel with CardLayout ======================== 
        cardLayout = new CardLayout(); // Allows switching between panels 
        mainPanel = new JPanel(cardLayout);

        // Initialize Panels 
        preferencesUI = new PreferencesUI(this);
        swipeUI = new SwipeUI(this, recommendationService); 
        likedRecipeUI = new LikedRecipeUI(this);
        homeDashboardUI = new HomeDashboardUI(this);

        mainPanel.add(homeDashboardUI, "Home");
        mainPanel.add(preferencesUI, "Preferences");
        mainPanel.add(swipeUI, "Swipe");
        mainPanel.add(likedRecipeUI, "LikedRecipes");

        add(mainPanel, BorderLayout.CENTER); // Add main panel below navbar

        cardLayout.show(mainPanel, "Home"); // Show Home Screen

        setVisible(true);
    }

    // ======================= UserService getter ========================
    public UserService getUserService() {
        return userService;
    }

    // ======================== Method to update navbar buttons based on login/guest ========================
    private void updateNavbarForUser() {
        boolean isGuest = UserSession.getInstance().isGuest() || currentUser == null;
        navbar.getLikedBtn().setToolTipText(isGuest ? "Login to view your liked recipes!" : null);
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

    // ======================== Method to switch to RecipeDetailsUI ========================
    public void showRecipeDetailsUI(Recipe recipe) {
        JFrame detailsFrame = new JFrame(recipe.getName());
        RecipeDetailsUI detailsUI = new RecipeDetailsUI(this, recipe);
        detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailsFrame.getContentPane().add(detailsUI);
        detailsFrame.pack();
        detailsFrame.setSize(1000, 700);
        detailsFrame.setLocationRelativeTo(null);
        detailsFrame.setVisible(true);
    }

    // ======================== Method to switch to LikedRecipesUI ========================
    public void showLikedRecipesUI() {
        cardLayout.show(mainPanel, "LikedRecipes");
    }

    // ======================== User getters/setters ========================
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ======================== Navbar Action Listener ========================
    private class NavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            
            if (source == navbar.getHomeBtn()) {
                cardLayout.show(mainPanel, "Home");

            } else if (source == navbar.getPreferencesBtn()) {
                cardLayout.show(mainPanel, "Preferences");

            } else if (source == navbar.getLikedBtn()) {
                if (UserSession.getInstance().isGuest() || currentUser == null) {
                    // Show dialog warning for guests
                    javax.swing.JOptionPane.showMessageDialog(
                        MainFrame.this,
                        "Please log in to view your liked recipes.",
                        "Login Required",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                   showLikedRecipesUI();
                }
                
            } else if (source == navbar.getMealMatchBtn()) {
                cardLayout.show(mainPanel, "Swipe");

            } else if (source == navbar.getLoginBtn()) {
                if (UserSession.getInstance().isGuest()) {
                    // Show login window for guests
                    JFrame loginFrame = new JFrame("Login");
                    loginFrame.setContentPane(new LoginUI());
                    loginFrame.pack();
                    loginFrame.setLocationRelativeTo(null);
                    loginFrame.setVisible(true);
                } else {
                    // Log out current user
                    UserSession.getInstance().logout();
                    currentUser = null;

                    // Redirect to Home
                    System.exit(0);
                }
            }
        }
    } 

    // Main method 
    public static void main(String[] args) {
        new MainFrame(UserSession.getInstance().getCurrentUser());
    }
}




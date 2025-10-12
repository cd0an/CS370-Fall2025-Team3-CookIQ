package cookiq.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class NavbarPanel extends JPanel {
    private JButton homeBtn;
    private JButton preferencesBtn;
    private JButton likedBtn;
    private JButton mealMatchBtn;
    private JButton loginBtn;

    public NavbarPanel(ActionListener listener) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        

        homeBtn = new JButton("Home");
        preferencesBtn = new JButton("Preferences");
        likedBtn = new JButton("View Liked Recipes");
        mealMatchBtn = new JButton("Meal Match");
        loginBtn = new JButton("Log In");

        JButton[] buttons = {homeBtn, preferencesBtn, likedBtn, mealMatchBtn, loginBtn};
        for (JButton btn : buttons) {
            
        }

    }

}


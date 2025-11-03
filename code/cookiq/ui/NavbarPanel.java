/* Navbar: Home, Prefereneces, View Liked Recipes, Meal Match, Log In or Out */

package cookiq.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import cookiq.services.UserSession;

public class NavbarPanel extends JPanel {
    private JButton homeBtn;
    private JButton preferencesBtn;
    private JButton likedBtn;
    private JButton mealMatchBtn;
    private JButton loginBtn;

    public NavbarPanel(ActionListener listener) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 100, 30));
        Font btnfont = new Font("SansSerif", Font.BOLD, 23);
        setBackground(new Color(0x6E, 0x92, 0x77)); // #6e9277
        setPreferredSize(new Dimension(0, 60)); 

        homeBtn = new JButton("Home");
        preferencesBtn = new JButton("Preferences");
        likedBtn = new JButton("View Liked Recipes");
        mealMatchBtn = new JButton("Meal Match");
           
        // If user is a guest, 'Log In' in navbar, otherwise Logout 
        loginBtn = new JButton(UserSession.getInstance().isGuest() ? "Log In" : "Log Out");

        JButton[] buttons = {homeBtn, preferencesBtn, likedBtn, mealMatchBtn, loginBtn};
        for (JButton btn : buttons) {
            btn.setForeground(Color.WHITE); 
            btn.setBackground(new Color(0x6E, 0x92, 0x77)); // #6e9277
            btn.setFocusPainted(false); // Removes blue outline
            btn.setContentAreaFilled(false); // Makes button background transparent 
            btn.setFont(new Font("SansSerif", Font.BOLD, 14));
            btn.setBorder(BorderFactory.createEmptyBorder()); // Removes box
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand on hover
            btn.addActionListener(listener); 

            btn.addMouseListener(new MouseAdapter() {
                 String originalText = btn.getText();

                @Override
                public void mouseEntered(MouseEvent e) {
                    // Add underline when hovering 
                    btn.setText("<html><u>" + originalText + "</u><html>"); 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setText(originalText);
                }
            });

            add(btn);
        }
    }

    public void updateLoginStatus() {
        boolean isGuest = UserSession.getInstance().isGuest();

        if (isGuest) {
            loginBtn.setText("Log In");
        } else {
            loginBtn.setText("Log Out");
        }
    }

    public JButton getHomeBtn() {return homeBtn;}
    public JButton getPreferencesBtn() {return preferencesBtn;}
    public JButton getLikedBtn() {return likedBtn;}
    public JButton getMealMatchBtn() {return mealMatchBtn;}
    public JButton getLoginBtn() {return loginBtn;}
}


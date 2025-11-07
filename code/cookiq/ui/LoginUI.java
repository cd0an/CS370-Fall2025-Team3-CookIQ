/**
 * LoginUI.java 
 *
 * Handles the login screen for CookIQ.
 * Collects username and password and passes them to UserService.
 */

package cookiq.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import cookiq.services.UserService;
import cookiq.services.UserSession;
import cookiq.models.User;

public class LoginUI extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private final UserService userService;

    public LoginUI() {
        userService = new UserService();
        setBackground(new Color(245, 240, 235));
        setLayout(new GridBagLayout());

        JPanel card = createCardPanel(420, 400);
        add(card);

        JLabel title = createTitle("Sign In");
        JLabel subtitle = createSubtitle("Welcome to CookIQ! Please enter your details.");
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));

        usernameField = createLabeledField(card, "Username", "Enter your username");
        passwordField = createLabeledPasswordField(card, "Password", "Enter your password");

        // Buttons
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginPanel.setBackground(Color.WHITE);

        JButton loginBtn = createRoundedButton("Login", new Color(90, 130, 100));
        loginBtn.addActionListener(e -> handleLogin());
        JButton guestBtn = createRoundedButton("Continue as Guest", new Color(150, 150, 150));
        guestBtn.addActionListener(e -> handleGuestLogin());
        loginPanel.add(loginBtn);
        loginPanel.add(guestBtn);

        card.add(Box.createVerticalStrut(25));
        card.add(loginPanel);

        // Sign‑up link
        JLabel signUpLabel = new JLabel("<html><u>Don't have an account? Sign up here.</u></html>");
        signUpLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signUpLabel.setForeground(new Color(60,50,40));
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginUI.this);
                if (frame != null) {
                    frame.setContentPane(new SignUpUI());
                    frame.revalidate();
                }
            }
        });

        card.add(Box.createVerticalStrut(15));
        card.add(signUpLabel);
        card.add(Box.createVerticalStrut(8));

        // Status
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please fill in both fields.", false);
            return;
        }

        if (!userService.loginUser(username, password)) {
            setStatus("Invalid username or password.", false);
            return;
        }

        // We always load in the users last preferences (if they logged in)
        User currentUser = new User(username, "");
        currentUser.getLikedRecipes().addAll(userService.getLikedRecipes(username));
        currentUser.getDislikedRecipes().addAll(userService.getDislikedRecipes(username));
        currentUser.getPreferences().copyPrefs(userService.getUserPreferences(username));
        UserSession.getInstance().login(currentUser);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginUI.this);
        if (frame != null) frame.dispose();
        new MainFrame(currentUser).setVisible(true);
    }

    private void handleGuestLogin() {
        UserSession.getInstance().loginAsGuest();
        JOptionPane.showMessageDialog(LoginUI.this,
                "Guest mode activated!\nYou can browse recipes but data won't be saved.",
                "Guest Mode", JOptionPane.INFORMATION_MESSAGE);
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginUI.this);
        if (frame != null) frame.dispose();
        new MainFrame(UserSession.getInstance().getCurrentUser()).setVisible(true);
    }

    private void setStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? new Color(50,120,70) : new Color(160,40,40));
    }

    // === Helpers ===
    private JPanel createCardPanel(int width, int height) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 210, 200), 1, true),
                new EmptyBorder(40, 50, 40, 50)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(width, height));
        return card;
    }

    private JLabel createTitle(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 28));
        label.setForeground(new Color(60,50,40));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(120,120,120));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createLabeledField(JPanel parent, String labelText, String placeholder) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(60,50,40));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createVerticalStrut(5));
        JTextField field = createStyledField(placeholder);
        parent.add(field);
        parent.add(Box.createVerticalStrut(15));
        return field;
    }

    private JPasswordField createLabeledPasswordField(JPanel parent, String labelText, String placeholder) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(60,50,40));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createVerticalStrut(5));
        JPasswordField field = createStyledPasswordField(placeholder);
        parent.add(field);
        parent.add(Box.createVerticalStrut(15));
        return field;
    }

    private JTextField createStyledField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(180,180,180));
                    g2.drawString(placeholder, getInsets().left, (getHeight()+getFontMetrics(getFont()).getAscent()-getFontMetrics(getFont()).getDescent())/2);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200,200,200),1,true), new EmptyBorder(10,12,10,12)));
        field.setPreferredSize(new Dimension(280,40));
        field.setMaximumSize(new Dimension(280,40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length==0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(180,180,180));
                    g2.drawString(placeholder, getInsets().left, (getHeight()+getFontMetrics(getFont()).getAscent()-getFontMetrics(getFont()).getDescent())/2);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200,200,200),1,true), new EmptyBorder(10,12,10,12)));
        field.setPreferredSize(new Dimension(280,40));
        field.setMaximumSize(new Dimension(280,40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD,13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10,18,10,18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt){ button.setBackground(color.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt){ button.setBackground(color); }
        });
        return button;
    }
}






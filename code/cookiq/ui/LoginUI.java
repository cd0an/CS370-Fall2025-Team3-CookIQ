/**
 * LoginUI.java 
 *
 * Handles the login screen for CookIQ.
 * Collects username and password and passes them to UserService.
 */

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
import org.bson.Document;
import cookiq.db.UserRepository;
import cookiq.security.PasswordUtils;
import cookiq.services.UserSession;

public class LoginUI extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel statusLabel;
    private UserRepository userRepository;

    public LoginUI() {
        userRepository = new UserRepository();

        Color BG = new Color(245, 240, 235);
        Color CARD = Color.WHITE;
        Color ACCENT = new Color(90, 130, 100);
        Color TEXT_DARK = new Color(60, 50, 40);
        Color TEXT_LIGHT = new Color(120, 120, 120);

        setBackground(BG);
        setLayout(new GridBagLayout());

        // === Main Card ===
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 210, 200), 1, true),
                new EmptyBorder(40, 50, 40, 50)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(420, 400));

        // === Title ===
        JLabel title = new JLabel("Sign-In", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Welcome to CookIQ! Please enter your details.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(TEXT_LIGHT);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Username Field ===
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        userLabel.setForeground(TEXT_DARK);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameField = createStyledField("Enter your username");

        // === Password Field ===
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        passLabel.setForeground(TEXT_DARK);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordField = createStyledPasswordField("Enter your password");

        // === Login Buttons ===
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        loginPanel.setBackground(CARD);

        JButton loginBtn = createRoundedButton("Login", ACCENT);
        loginBtn.addActionListener(e -> handleLogin());
        loginPanel.add(loginBtn);

        // === Guest Mode Button ===
        JButton guestBtn = createRoundedButton("Continue as Guest", new Color(150, 150, 150));
        guestBtn.addActionListener(e -> {
            UserSession.getInstance().loginAsGuest();
            JOptionPane.showMessageDialog(LoginUI.this,
                    "Guest mode activated!\nYou can browse recipes but data won't be saved.",
                    "Guest Mode", JOptionPane.INFORMATION_MESSAGE);
            new MainFrame().setVisible(true);
            SwingUtilities.getWindowAncestor(LoginUI.this).setVisible(false);
        });
        loginPanel.add(guestBtn);

        // === Sign Up Label ===
        JLabel signUpLabel = new JLabel("<html><u>Don't have an account? Sign up here.</u></html>");
        signUpLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        signUpLabel.setForeground(TEXT_DARK);
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

        // === Status Label ===
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Add Components ===
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(25));
        card.add(loginPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(signUpLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(statusLabel);

        add(card);
    }

    // === Helper to create styled text field with placeholder ===
    private JTextField createStyledField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(180, 180, 180));
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    Insets insets = getInsets();
                    g2.drawString(placeholder, insets.left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        field.setPreferredSize(new Dimension(280, 40));
        field.setMaximumSize(new Dimension(280, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    // === Helper to create styled password field with placeholder ===
    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(180, 180, 180));
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    Insets insets = getInsets();
                    g2.drawString(placeholder, insets.left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true),
                new EmptyBorder(10, 12, 10, 12)));
        field.setPreferredSize(new Dimension(280, 40));
        field.setMaximumSize(new Dimension(280, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    // === Rounded Button ===
    private JButton createRoundedButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        return button;
    }

    // === Handle Login ===
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please fill in both fields.", false);
            return;
        }

        Document user = userRepository.getUser(username);
        if (user == null) {
            setStatus("No account found for " + username + ".", false);
            return;
        }

        String storedHash = user.getString("passwordHash");
        String enteredHash = PasswordUtils.sha256(password);

        if (PasswordUtils.slowEquals(storedHash, enteredHash)) {
            setStatus("Welcome, " + username + "!", true);

            cookiq.models.User currentUser =
                    new cookiq.models.User(username, "", ""); // avoid storing plain password
            UserSession.getInstance().login(currentUser);

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LoginUI.this);
            if (frame != null) frame.dispose();
            new MainFrame().setVisible(true);

        } else {
            setStatus("Incorrect password.", false);
        }
    }

    // === Status Update ===
    private void setStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? new Color(50, 120, 70) : new Color(160, 40, 40));
    }
}


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
/**
 * LoginUI.java
 *
 * Finalized Login Page UI for CookIQ.
 * Adds inline status/error label, input validation, and smoother UX.
 */

package cookiq.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import org.bson.Document;
import cookiq.db.UserRepository;
import cookiq.security.PasswordUtils;

public class LoginUI extends JFrame {
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

        setTitle("CookIQ - Sign In");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 420);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 210, 200), 1, true),
                new EmptyBorder(30, 40, 30, 40)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(380, 340));

        JLabel title = new JLabel("Sign-In", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea subtitle = new JTextArea("Welcome to CookIQ! Please enter your details.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_DARK);
        subtitle.setLineWrap(true); //wrap lines
        subtitle.setWrapStyleWord(true); //wrap at word boundaries
        subtitle.setEditable(false); //make it read-only
        subtitle.setOpaque(false); //transparent background
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new JTextField();
        styleField(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField = new JPasswordField();
        styleField(passwordField);

        // Centered login button
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(CARD);
        JButton loginBtn = createRoundedButton("Login", ACCENT);
        loginBtn.addActionListener(e -> handleLogin());
        loginPanel.add(loginBtn);
        
        // Updated link style
        JLabel signUpLabel = new JLabel("No account yet? Register here.");
        signUpLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        signUpLabel.setForeground(TEXT_DARK);
        signUpLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new SignUpUI().setVisible(true);
            }
        });

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(userLabel);
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));
        card.add(passLabel);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(20));
        card.add(loginPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(signUpLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);

        add(card);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
        field.setPreferredSize(new Dimension(250, 35));
    }

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
        button.setFont(new Font("SansSerif", Font.BOLD, 15));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);

        // hover
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

        if (PasswordUtils.slowEquals(storedHash, enteredHash))
            setStatus("Welcome, " + username + "!", true);
        else
            setStatus("Incorrect password.", false);
    }

    private void setStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? new Color(50, 120, 70) : new Color(160, 40, 40));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}

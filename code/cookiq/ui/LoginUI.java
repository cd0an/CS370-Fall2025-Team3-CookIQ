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

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bson.Document;
import cookiq.db.UserRepository;

public class LoginUI extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private UserRepository userRepository;

    public LoginUI() {
        userRepository = new UserRepository(); // Connects through MongoDBConnection

        // === Frame setup ===
        setTitle("CookIQ - Login");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(248, 244, 240));
        setLayout(new BorderLayout());

        // === Title ===
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(new Color(80, 70, 60));
        add(titleLabel, BorderLayout.NORTH);

        // === Center form panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 244, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usernameField.setToolTipText("Enter your CookIQ username");
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passwordField.setToolTipText("Enter your password");
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(new Color(0x6E, 0x92, 0x77));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        // Inline status label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(160, 30, 30));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(statusLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // === Register link ===
        JLabel registerLabel = new JLabel("No account yet? Register here.", SwingConstants.CENTER);
        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        registerLabel.setForeground(new Color(70, 100, 150));
        add(registerLabel, BorderLayout.SOUTH);

        // === Event hooks ===
        usernameField.requestFocusInWindow();

        // Press Enter in password field
        passwordField.addActionListener(e -> loginButton.doClick());

        // Clear status when user types again
        DocumentListener clearer = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                clearStatus();
            }

            public void removeUpdate(DocumentEvent e) {
                clearStatus();
            }

            public void changedUpdate(DocumentEvent e) {
                clearStatus();
            }
        };
        usernameField.getDocument().addDocumentListener(clearer);
        passwordField.getDocument().addDocumentListener(clearer);

        // Login button action
        loginButton.addActionListener(e -> handleLogin());

        // Register click action
        registerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                handleRegister();
            }
        });
    }

    /** Handle user login using MongoDB */
    private void handleLogin() {
        clearStatus();

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            setStatus("Please enter both username and password.", false);
            return;
        }

        setBusy(true);
        try {
            Document userDoc = userRepository.findUserByUsername(username);

            if (userDoc != null) {
                String storedPassword = userDoc.getString("password");

                if (storedPassword.equals(password)) {
                    setStatus("Welcome, " + username + "!", true);
                    // 🔜 Later: new HomeUI(username).setVisible(true);
                    // dispose();
                } else {
                    setStatus("Incorrect password. Try again.", false);
                }
            } else {
                setStatus("No account found for " + username + ".", false);
            }

        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Database error. Please try again later.", false);
        } finally {
            setBusy(false);
        }
    }

    /** Handle user registration via MongoDB */
    private void handleRegister() {
        String newUsername = JOptionPane.showInputDialog("Enter a new username:");
        if (newUsername == null || newUsername.trim().isEmpty())
            return;

        String newPassword = JOptionPane.showInputDialog("Enter a new password:");
        if (newPassword == null || newPassword.trim().isEmpty())
            return;

        try {
            Document existing = userRepository.findUserByUsername(newUsername.trim());
            if (existing != null) {
                JOptionPane.showMessageDialog(null, "Username already exists!");
                return;
            }

            Document userDoc = new Document("username", newUsername.trim())
                    .append("password", newPassword.trim())
                    .append("preferences", "")
                    .append("liked", new ArrayList<String>())
                    .append("disliked", new ArrayList<String>());

            userRepository.createUser(userDoc);
            JOptionPane.showMessageDialog(null, "Registration successful!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error creating user in MongoDB!");
        }
    }

    /** Helpers for status and UI feedback */
    private void setStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? new Color(35, 120, 60) : new Color(160, 30, 30));
    }

    private void clearStatus() {
        statusLabel.setText(" ");
    }

    private void setBusy(boolean busy) {
        loginButton.setEnabled(!busy);
        usernameField.setEnabled(!busy);
        passwordField.setEnabled(!busy);
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR)
                : Cursor.getDefaultCursor());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
package cookiq.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import cookiq.db.UserRepository;

public class SignUpUI extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel statusLabel;
    private UserRepository userRepository;

    public SignUpUI() {
        userRepository = new UserRepository();

        Color BG = new Color(245, 240, 235);
        Color CARD = Color.WHITE;
        Color ACCENT = new Color(90, 130, 100);
        Color TEXT_DARK = new Color(60, 50, 40);

        // === Root panel (this) ===
        setBackground(BG);
        setLayout(new GridBagLayout());

        // ===== MAIN CARD =====
        JPanel card = new JPanel();
        card.setBackground(CARD);

        // Made the border padding larger & softer
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 210, 200), 1, true),
                new EmptyBorder(60, 70, 60, 70) // increased padding for more space inside
        ));

        // Using BoxLayout for vertical stacking
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Increased preferred size to make the card visually larger
        card.setPreferredSize(new Dimension(450, 550)); // was 380x430 before

        // === Title ===
        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Please sign up to continue.", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Form ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        userLabel.setForeground(TEXT_DARK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        usernameField = new JTextField();
        styleField(usernameField);
        gbc.gridy = 1;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        passLabel.setForeground(TEXT_DARK);
        gbc.gridy = 2;
        formPanel.add(passLabel, gbc);

        passwordField = new JPasswordField();
        styleField(passwordField);
        gbc.gridy = 3;
        formPanel.add(passwordField, gbc);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        confirmLabel.setForeground(TEXT_DARK);
        gbc.gridy = 4;
        formPanel.add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField();
        styleField(confirmPasswordField);
        gbc.gridy = 5;
        formPanel.add(confirmPasswordField, gbc);

        // === Centered Sign Up button ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(CARD);
        JButton signUpBtn = createRoundedButton("Sign Up", ACCENT);
        signUpBtn.addActionListener(e -> handleSignUp());
        buttonPanel.add(signUpBtn);

        JLabel loginLabel = new JLabel("Already have an account? Login here.");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        loginLabel.setForeground(TEXT_DARK);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // To switch panels, you can trigger a parent controller instead of creating new
                // frames.
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SignUpUI.this);
                if (frame != null) {
                    frame.setContentPane(new LoginUI()); // replace content
                    frame.revalidate();
                }
            }
        });

        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Add to Card ===
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(15));
        card.add(formPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(buttonPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(loginLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(statusLabel);

        add(card);
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(180, 180, 180), 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        field.setPreferredSize(new Dimension(300, 35));
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
        button.setBorder(new EmptyBorder(10, 25, 10, 25));
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

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            setStatus("Please fill in all fields.", false);
            return;
        }

        if (!password.equals(confirm)) {
            setStatus("Passwords do not match.", false);
            return;
        }

        boolean success = userRepository.registerUser(username, password);
        if (success) {
            JOptionPane.showMessageDialog(this, "Account created successfully! You can now log in.");
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.setContentPane(new LoginUI());
                frame.revalidate();
            }
        } else {
            setStatus("Username already exists.", false);
        }
    }

    private void setStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? new Color(50, 120, 70) : new Color(160, 40, 40));
    }
}

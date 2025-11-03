package cookiq.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import cookiq.db.UserRepository;
import cookiq.security.PasswordUtils;

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
        Color TEXT_LIGHT = new Color(120, 120, 120);

        setBackground(BG);
        setLayout(new GridBagLayout());

        // ===== Main Card =====
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 210, 200), 1, true),
                new EmptyBorder(40, 50, 40, 50)
        ));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(450, 520));

        // === Title ===
        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Create your account to start cooking!", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
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

        // === Confirm Password Field ===
        JLabel confirmLabel = new JLabel("Confirm Password");
        confirmLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        confirmLabel.setForeground(TEXT_DARK);
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        confirmPasswordField = createStyledPasswordField("Confirm your password");

        // === Buttons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(CARD);

        JButton signUpBtn = createRoundedButton("Sign Up", ACCENT);
        signUpBtn.addActionListener(e -> handleSignUp());
        buttonPanel.add(signUpBtn);

        // === Login Label ===
        JLabel loginLabel = new JLabel("<html><u>Already have an account? Login here.</u></html>");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginLabel.setForeground(TEXT_DARK);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SignUpUI.this);
                if (frame != null) {
                    frame.setContentPane(new LoginUI());
                    frame.revalidate();
                }
            }
        });

        // === Status Label ===
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === Add Components to Card ===
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(15));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(15));
        card.add(confirmLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(confirmPasswordField);
        card.add(Box.createVerticalStrut(20));
        card.add(buttonPanel);
        card.add(Box.createVerticalStrut(15));
        card.add(loginLabel);
        card.add(Box.createVerticalStrut(8));
        card.add(statusLabel);

        add(card);
    }

    // === Styled JTextField ===
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
                new EmptyBorder(10, 12, 10, 12)
        ));
        field.setPreferredSize(new Dimension(280, 40));
        field.setMaximumSize(new Dimension(280, 40));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        return field;
    }

    // === Styled JPasswordField ===
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
                new EmptyBorder(10, 12, 10, 12)
        ));
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
            public void mouseEntered(java.awt.event.MouseEvent evt) { button.setBackground(color.darker()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { button.setBackground(color); }
        });
        return button;
    }

    // === Handle Sign-Up ===
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
            JOptionPane.showMessageDialog(this,
                    "Account created successfully! You can now log in.");
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.setContentPane(new LoginUI());
                frame.revalidate();
            }
        } else {
            setStatus("Username already exists.", false);
        }
    }

    // === Update Status ===
    private void setStatus(String msg, boolean success) {
        statusLabel.setText(msg);
        statusLabel.setForeground(success ? new Color(50, 120, 70) : new Color(160, 40, 40));
    }
}

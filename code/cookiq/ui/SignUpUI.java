package cookiq.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import cookiq.models.User;
import cookiq.services.UserService;
import cookiq.services.UserSession;

public class SignUpUI extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;
    private JLabel statusLabel;
    private final UserService userService;

    public SignUpUI() {
        userService = new UserService();

        setBackground(new Color(245, 240, 235));
        setLayout(new GridBagLayout());

        JPanel card = createCardPanel(420, 450);
        add(card);

        JLabel title = createTitle("Register");
        JLabel subtitle = createSubtitle("Sign up to continue cooking!");
        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(25));

        usernameField = createLabeledField(card, "Username", "Choose a username");
        passwordField = createLabeledPasswordField(card, "Password", "Enter a password");
        confirmField = createLabeledPasswordField(card, "Confirm Password", "Re-enter your password");

        // Sign Up Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        JButton signUpBtn = createRoundedButton("Sign Up", new Color(90, 130, 100));
        signUpBtn.addActionListener(e -> handleSignUp());
        buttonPanel.add(signUpBtn);

        card.add(Box.createVerticalStrut(25));
        card.add(buttonPanel);

        // Back to Login Link
        JLabel loginLabel = new JLabel("<html><u>Already have an account? Login here.</u></html>");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginLabel.setForeground(new Color(60,50,40));
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

        card.add(Box.createVerticalStrut(15));
        card.add(loginLabel);
        card.add(Box.createVerticalStrut(8));

        // Status Label
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(statusLabel);
    }

    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            setStatus("Please fill in all fields.", false);
            return;
        }

        if (!password.equals(confirm)) {
            setStatus("Passwords do not match.", false);
            return;
        }

        boolean success = userService.registerUser(username, password);
        if (!success) {
            setStatus("Username already exists.", false);
            return;
        }

        // Auto login after registration
        User newUser = new User(username, "");
        UserSession.getInstance().login(newUser);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(SignUpUI.this);
        if (frame != null) frame.dispose();
        new MainFrame(newUser).setVisible(true);
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




/**
 * SwipeUI.java
 *
 * Shows recommended recipes in a swipe/like-dislike interface.
 * Sends user feedback to FeedbackService to improve future recommendations.
 */


package cookiq.ui;

import cookiq.models.Preferences;
import cookiq.db.RecipeRepository;
import org.bson.Document;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cookiq.db.RecipeRepository;

public class SwipeUI extends JPanel {
    private JLabel titleLabel, tagsLabel;
    private JButton viewRecipeBtn, likeBtn, dislikeBtn;
    private JPanel recipeCard;
    private List<String[]> recipes;
    private int currentIndex; 

    public SwipeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0xF2, 0xEF, 0xEB)); // #f2efeb

        // ====================== Fetch User Preferences ======================



        currentIndex = 0;

        // ====================== Recipe Card Panel ======================
        recipeCard = new RoundedPanel(25, Color.WHITE); //#c2b19c 
        recipeCard.setBackground(Color.WHITE);
        recipeCard.setLayout(new BoxLayout(recipeCard, BoxLayout.Y_AXIS));
        recipeCard.setPreferredSize(new Dimension(480, 480));
        recipeCard.setMaximumSize(new Dimension(480, 480));
        recipeCard.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Recipe Title 
        titleLabel = new JLabel(recipes.get(currentIndex)[0], SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Recipe Tags
        tagsLabel = new JLabel(recipes.get(currentIndex)[1], SwingConstants.CENTER);
        tagsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tagsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // View Recipe Button 
        viewRecipeBtn = new JButton("View Full Recipe");
        viewRecipeBtn.setBackground(new Color(0x6E, 0x92, 0x77)); // #6e9277
        viewRecipeBtn.setForeground(Color.WHITE);
        viewRecipeBtn.setFocusPainted(false);
        viewRecipeBtn.setContentAreaFilled(true);
        viewRecipeBtn.setOpaque(true);
        viewRecipeBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        viewRecipeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover Effect
        viewRecipeBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                viewRecipeBtn.setBackground(new Color(0x5A, 0x7B, 0x63));
            }
            public void mouseExited(MouseEvent e) {
                viewRecipeBtn.setBackground(new Color(0x6E, 0x92, 0x77));
            }
        });

        recipeCard.add(Box.createVerticalGlue());
        recipeCard.add(titleLabel);
        recipeCard.add(tagsLabel);
        recipeCard.add(Box.createRigidArea(new Dimension(0, 15)));
        recipeCard.add(viewRecipeBtn);
        recipeCard.add(Box.createVerticalGlue());

        // ====================== Like / Dislike Buttons ======================
        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(new Color(0xF2, 0xEF, 0xEB)); 
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Dislike button 
        dislikeBtn = new JButton("DISLIKE");
        dislikeBtn.setBackground(new Color(0xD9, 0x7A, 0x7A));
        dislikeBtn.setForeground(Color.WHITE);
        dislikeBtn.setOpaque(true);
        dislikeBtn.setBorderPainted(false);

        // Like button 
        likeBtn = new JButton("LIKE");
        likeBtn.setBackground(new Color(0x7FAE85));
        likeBtn.setForeground(Color.WHITE);
        likeBtn.setOpaque(true);
        likeBtn.setBorderPainted(false);

        for (JButton btn : new JButton[] {dislikeBtn, likeBtn}) {
            btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(130, 45));
        }

        dislikeBtn.addActionListener(e -> nextRecipe());
        likeBtn.addActionListener(e -> nextRecipe());

        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(dislikeBtn);
        actionPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        actionPanel.add(likeBtn);
        actionPanel.add(Box.createHorizontalGlue());

        // ====================== Combined Center Panel (Card + Buttons stacked) ======================
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(0xF2, 0xEF, 0xEB));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(recipeCard);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 8))); 
        centerPanel.add(actionPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
    }

    // Function to update recipe 
    private void updateRecipe() {
        titleLabel.setText(recipes.get(currentIndex)[0]);
        tagsLabel.setText(recipes.get(currentIndex)[1]);
    }

    // Function to go to next recipe when the like or dislike button is clicked 
    private void nextRecipe() {
        currentIndex = (currentIndex + 1) % recipes.size();
        updateRecipe(); 
    }

    // ====================== Custom Rounded Panel ======================
    private static class RoundedPanel extends JPanel {
        private final int cornerRadius;
        private final Color borderColor;

        public RoundedPanel(int radius, Color borderColor) {
            this.cornerRadius = radius;
            this.borderColor = borderColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.setColor(borderColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
    }
}








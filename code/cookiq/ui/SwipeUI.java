/**
 * SwipeUI.java
 *
 * Shows recommended recipes in a swipe/like-dislike interface.
 * Sends user feedback to FeedbackService to improve future recommendations.
 */

package cookiq.ui;

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

import cookiq.models.Preferences;

public class SwipeUI extends JPanel {
    private JLabel titleLabel, tagsLabel;
    private JButton viewRecipeBtn, likeBtn, dislikeBtn;
    private JPanel recipeCard;
    private List<String[]> recipes;
    private List<String[]> likedRecipes = new ArrayList<>();
    private int currentIndex; 
    private Preferences userPreferences; // Store user preferences 

    // Constructor 
    public SwipeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0xF2, 0xEF, 0xEB)); // #f2efeb

        // ====================== Fetch User Preferences from MongoDB (Dummy Test Rn) ======================
        recipes = new ArrayList<>();

        recipes.add(new String[]{"Mediterranean Pasta Bowl", "Vegetarian • Low-Calorie"});
        recipes.add(new String[]{"Spicy Chicken Tacos", "High-Protein • Gluten-Free"});
        recipes.add(new String[]{"Avocado Toast", "Vegan • Low-Calorie"});
        recipes.add(new String[]{"Beef Stir-Fry", "High-Calorie • Protein-Rich"});
        recipes.add(new String[]{"Shrimp Fried Rice", "Asian • Gluten-Free"});
        recipes.add(new String[]{"Quinoa Salad", "Vegetarian • High-Protein"});
      
            // To be Replaced 
            /*
            * List <Document> recipeDocs = RecipeRepository.getRecipesByPreferences(userPreferences);
            * for (Document doc : recipeDocs) {
            *      String name = doc.getString("name");
            *      String tags = doc.getString("tags");
            *      recipes.add(new String[]{name, tags});
            * }
            */

        currentIndex = 0;

        initSwipeUI();
    }

    // Function to handle the UI
    private void initSwipeUI() {
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
        dislikeBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dislikeBtn.setFocusPainted(false);
        dislikeBtn.setPreferredSize(new Dimension(140, 45));

        // Hover effect for Dislike Button
        dislikeBtn.addMouseListener(new MouseAdapter() {
            Color originalColor = dislikeBtn.getBackground();
            Dimension originalSize = dislikeBtn.getPreferredSize();

            @Override
            public void mouseEntered(MouseEvent e) {
                dislikeBtn.setBackground(new Color(0xC94F4F));
                dislikeBtn.setPreferredSize(new Dimension(originalSize.width + 10, originalSize.height + 5));
                dislikeBtn.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                dislikeBtn.setBackground(originalColor);
                dislikeBtn.setPreferredSize(originalSize);
                dislikeBtn.revalidate();
            }
        });

        // Like button 
        likeBtn = new JButton("LIKE");
        likeBtn.setBackground(new Color(0x7FAE85));
        likeBtn.setForeground(Color.WHITE);
        likeBtn.setOpaque(true);
        likeBtn.setBorderPainted(false);
        likeBtn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        likeBtn.setFocusPainted(false);
        likeBtn.setPreferredSize(new Dimension(140, 45));

        // Hover effect for Like Button
        likeBtn.addMouseListener(new MouseAdapter() {
            Color originalColor = likeBtn.getBackground();
            Dimension originalSize = likeBtn.getPreferredSize();

            @Override
            public void mouseEntered(MouseEvent e) {
                likeBtn.setBackground(new Color(0x5A, 0x7B, 0x63));
                likeBtn.setPreferredSize(new Dimension(originalSize.width + 10, originalSize.height + 5));
                likeBtn.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                likeBtn.setBackground(originalColor);
                likeBtn.setPreferredSize(originalSize);
                likeBtn.revalidate();
            }
        });

        dislikeBtn.addActionListener(e -> nextRecipe(false));
        likeBtn.addActionListener(e -> nextRecipe(true));

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

    // ====================== Next Recipe ======================
    private void nextRecipe(boolean liked) {
        if (liked) {
            likedRecipes.add(recipes.get(currentIndex));
        }

        currentIndex++;
        if (currentIndex >= recipes.size()) {
            showEndOfQueueUI();
        } else {
            updateRecipe();
        }
    }

    // Function to update recipe 
    private void updateRecipe() {
        titleLabel.setText(recipes.get(currentIndex)[0]);
        tagsLabel.setText(recipes.get(currentIndex)[1]);
    }

    // ====================== End of Queue UI ======================
    // Function to show end of queue  
    private void showEndOfQueueUI() {
        removeAll();
        revalidate();
        repaint();

        // Keep the white recipe panel
        recipeCard.removeAll();

        // Message at the top 
        JLabel message = new JLabel("You've reached the end of your recipes!", SwingConstants.CENTER);
        message.setFont(new Font("SansSerif", Font.BOLD, 18));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons
        JButton viewLiked = new JButton("View Liked Recipes");
        JButton newSuggestions = new JButton("Request New Suggestions");
        JButton resetPrefs = new JButton("Reset Preferences");

        // Styling 
        JButton[] buttons = {viewLiked, newSuggestions, resetPrefs};
        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setBackground(new Color(0x6E, 0x92, 0x77)); 
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setOpaque(true);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
            btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        }

        viewLiked.addActionListener(e -> System.out.println("Open liked recipes panel"));
        newSuggestions.addActionListener(e -> System.out.println("Fetch new suggestions"));
        resetPrefs.addActionListener(e -> System.out.println("Go back to preferences"));

        // Add components to the white panel 
        recipeCard.setLayout(new BoxLayout(recipeCard, BoxLayout.Y_AXIS));
        recipeCard.add(Box.createVerticalGlue());
        recipeCard.add(message);
        recipeCard.add(Box.createRigidArea(new Dimension(0, 12)));
        recipeCard.add(viewLiked);
        recipeCard.add(Box.createRigidArea(new Dimension(0, 12)));
        recipeCard.add(newSuggestions);
        recipeCard.add(Box.createRigidArea(new Dimension(0, 12)));
        recipeCard.add(resetPrefs);
        recipeCard.add(Box.createVerticalGlue());

        // Add the panel to the main center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(0xF2, 0xEF, 0xEB));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(recipeCard);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
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

    public void setUserPreferences(Preferences prefs) {
        this.userPreferences = prefs;
        System.out.println("User preferences received: " + prefs);
    }
}








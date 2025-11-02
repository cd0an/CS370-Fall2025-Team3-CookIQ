/* View All Liked Recipes UI */

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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import cookiq.models.Recipe;

public class LikedRecipesUI extends JPanel {

    private JPanel likedRecipesPanel;
    private JScrollPane scrollPane;
    private List<String[]> likedRecipes; // {title, tags, cuisine, cookTime, cost}
    private MainFrame mainFrame; // reference to MainFrame for navigation

    public LikedRecipesUI(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        likedRecipes = new ArrayList<>();

        setLayout(new BorderLayout());
        setBackground(new Color(0xF2, 0xEF, 0xEB));

        // Header
        JLabel header = new JLabel("Favorites", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // Panel for liked recipe cards
        likedRecipesPanel = new JPanel();
        likedRecipesPanel.setLayout(new BoxLayout(likedRecipesPanel, BoxLayout.Y_AXIS));
        likedRecipesPanel.setBackground(new Color(0xF2, 0xEF, 0xEB));
        likedRecipesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane = new JScrollPane(likedRecipesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Add a liked recipe card
    public void addLikedRecipe(String title, String tags, String cuisine, String cookTime, String cost) {
        JPanel card = createRecipeCard(title, tags, cuisine, cookTime, cost);
        likedRecipesPanel.add(card);
        likedRecipesPanel.add(Box.createVerticalStrut(20));
        likedRecipesPanel.revalidate();
        likedRecipesPanel.repaint();
    }

    // Create a single recipe card panel (SwipeUI style)
    private JPanel createRecipeCard(String title, String tags, String cuisine, String cookTime, String cost) {
        JPanel card = new RoundedPanel(25, Color.WHITE);
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 480));
        card.setMaximumSize(new Dimension(480, 480));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Preview Image
        JLabel imageLabel = new JLabel("Recipe Image", SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(420, 250));
        imageLabel.setMaximumSize(new Dimension(420, 250));
        card.add(imageLabel);
        card.add(Box.createVerticalStrut(15));

        // Recipe Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));

        // Cuisine | Cook Time | Cost
        JLabel infoLabel = new JLabel(cuisine + " | " + cookTime + " | " + cost, SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(infoLabel);
        card.add(Box.createVerticalStrut(12));

        // Recipe Tags
        JLabel tagsLabel = new JLabel(tags, SwingConstants.CENTER);
        tagsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tagsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(tagsLabel);
        card.add(Box.createVerticalStrut(20));

        // View Full Recipe Button
        JButton viewRecipeBtn = new JButton("View Full Recipe");
        viewRecipeBtn.setBackground(new Color(0x6E, 0x92, 0x77));
        viewRecipeBtn.setForeground(Color.WHITE);
        viewRecipeBtn.setFocusPainted(false);
        viewRecipeBtn.setContentAreaFilled(true);
        viewRecipeBtn.setOpaque(true);
        viewRecipeBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        viewRecipeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect
        viewRecipeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                viewRecipeBtn.setBackground(new Color(0x5A, 0x7B, 0x63));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                viewRecipeBtn.setBackground(new Color(0x6E, 0x92, 0x77));
            }
        });

        // Button navigates to RecipeDetailsUI
        viewRecipeBtn.addActionListener(e -> {
            Recipe selectedRecipe = new Recipe(
                "1",           // id dummy
                title,
                cuisine,
                tags,
                Integer.parseInt(cookTime.replaceAll("[^0-9]", "")),
                Double.parseDouble(cost.replaceAll("[^0-9.]", "")),
                420,           // calories dummy
                List.of("Ingredient 1", "Ingredient 2"),  // dummy ingredients
                List.of("Step 1", "Step 2"),              // dummy directions
                null           // image
            );
            mainFrame.showRecipeDetailsUI(selectedRecipe);
        });

        card.add(viewRecipeBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    // Rounded panel for styling
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

    // For testing only
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Liked Recipes UI Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            MainFrame dummyMain = new MainFrame(); // you can mock or implement minimal MainFrame
            LikedRecipesUI likedUI = new LikedRecipesUI(dummyMain);

            // Add dummy liked recipes
            likedUI.addLikedRecipe("Mediterranean Pasta Bowl", "Vegetarian • Low-Calorie", "Mediterranean", "25 min", "$12.50");
            likedUI.addLikedRecipe("Spicy Chicken Tacos", "High-Protein • Gluten-Free", "Mexican", "30 min", "$15.00");
            likedUI.addLikedRecipe("Avocado Toast", "Vegan • Low-Calorie", "American", "10 min", "$8.00");

            frame.setContentPane(likedUI);
            frame.setVisible(true);
        });
    }
}



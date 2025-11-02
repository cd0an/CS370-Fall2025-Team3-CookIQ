/* View All Liked Recipes UI */

package cookiq.ui;

import java.awt.*;
import javax.swing.*;
import java.util.List;
import cookiq.models.Recipe;
import java.util.ArrayList;

public class LikedRecipeUI extends JPanel {
    private JPanel likedRecipesPanel; // Container for all liked recipe cards
    private JScrollPane scrollPane; // Scrolls when there are many recipes 
    private List<String[]> likedRecipes; // Each recipe: {title, tags, cuisine, cookTime, cost}
    private MainFrame mainFrame; // Reference to MainFrame for navigation

    // Constructor
    public LikedRecipeUI(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        likedRecipes = new ArrayList<>();

        // Main layout setup
        setLayout(new BorderLayout()); 
        setBackground(new Color(0xF2, 0xEf, 0xEB)); // Light Orange

        // ========= Title ========= 
        JLabel header = new JLabel("Favorites", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(header, BorderLayout.NORTH);

        // ========= Liked Recipe Panel ========= 
        likedRecipesPanel = new JPanel();
        likedRecipesPanel.setLayout(new BoxLayout(likedRecipesPanel, BoxLayout.Y_AXIS));
        likedRecipesPanel.setBackground(new Color(0xF2, 0xEf, 0xEB));
        likedRecipesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add scrollable container for the cards
        scrollPane = new JScrollPane(likedRecipesPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

    }

    // ========= Add a Liked Recipe Card ========= 
    // Dynamically adds a recipe card to the list
    public void addLikedRecipe(String title, String tags, String cuisine, String cookTime, String cost) {
        JPanel card = createRecipeCard(title, tags, cuisine, cookTime, cost);
        likedRecipesPanel.add(card);
        likedRecipesPanel.add(Box.createVerticalStrut(20)); // Space between cards
        likedRecipesPanel.revalidate();
        likedRecipesPanel.repaint();
    }

    // Create a Single Recipe Card
    private JPanel createRecipeCard(String title, String tags, String cuisine, String cookTime, String cost) {
        JPanel card = new RoundedPanel(25, Color.WHITE);
        card.setBackground(Color.WHITE);
        card.setOpaque(true);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(480, 480));
        card.setMaximumSize(new Dimension(480, 480));
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // --- Recipe Image Placeholder ---
        JLabel imageLabel = new JLabel("Recipe Image", SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(420, 250));
        imageLabel.setMaximumSize(new Dimension(420, 250));
        card.add(imageLabel);
        card.add(Box.createVerticalStrut(15));

        // --- Recipe Title ---
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(15));

        // --- Cuisine | Cook Time | Cost ---
        JLabel infoLabel = new JLabel(cuisine + " | " + cookTime + " | " + cost, SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(infoLabel);
        card.add(Box.createVerticalStrut(12));

        // --- Recipe Tags ---
        JLabel tagsLabel = new JLabel(tags, SwingConstants.CENTER);
        tagsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tagsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(tagsLabel);
        card.add(Box.createVerticalStrut(20));

        // --- "View Full Recipe" Button ---
        JButton viewRecipeBtn = new JButton("View Full Recipe");
        viewRecipeBtn.setBackground(new Color(0x6E, 0x92, 0x77)); // Soft green
        viewRecipeBtn.setForeground(Color.WHITE);
        viewRecipeBtn.setFocusPainted(false);
        viewRecipeBtn.setContentAreaFilled(true);
        viewRecipeBtn.setOpaque(true);
        viewRecipeBtn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        viewRecipeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover effect for button
        viewRecipeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                viewRecipeBtn.setBackground(new Color(0x5A, 0x7B, 0x63));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                viewRecipeBtn.setBackground(new Color(0x6E, 0x92, 0x77));
            }
        });

        // --- Action: Navigate to RecipeDetailsUI when clicked ---
        viewRecipeBtn.addActionListener(e -> {
            Recipe selectedRecipe = new Recipe(
                "1", // Dummy ID
                title,
                cuisine,
                tags,
                Integer.parseInt(cookTime.replaceAll("[^0-9]", "")),
                Double.parseDouble(cost.replaceAll("[^0-9.]", "")),
                420, // Dummy calories
                List.of("Ingredient 1", "Ingredient 2"),
                List.of("Step 1", "Step 2"),
                null // No image
            );
            mainFrame.showRecipeDetailsUI(selectedRecipe);
        });

        card.add(viewRecipeBtn);
        card.add(Box.createVerticalGlue());

        return card;
    }

    //  ========= Custom Rounded Panel ========= 
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

    // ===== Test Main Method =====
    // Displays the UI independently for testing purposes
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Liked Recipes UI Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            MainFrame dummyMain = new MainFrame(); // Mock main frame
            LikedRecipeUI likedUI = new LikedRecipeUI(dummyMain);

            // Add some sample liked recipes
            likedUI.addLikedRecipe("Mediterranean Pasta Bowl", "Vegetarian • Low-Calorie", "Mediterranean", "25 min", "$12.50");
            likedUI.addLikedRecipe("Spicy Chicken Tacos", "High-Protein • Gluten-Free", "Mexican", "30 min", "$15.00");
            likedUI.addLikedRecipe("Avocado Toast", "Vegan • Low-Calorie", "American", "10 min", "$8.00");

            frame.setContentPane(likedUI);
            frame.setVisible(true);
        });
    }
}



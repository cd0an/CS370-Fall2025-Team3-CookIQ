/**
 * SwipeUI.java
 *
 * Shows recommended recipes in a swipe/like-dislike interface.
 * Sends user feedback to FeedbackService to improve future recommendations.
 */

package cookiq.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SwipeUI extends JPanel {
    private JLabel titleLabel, tagsLabel;
    private JButton viewRecipeBtn, likeBtn, dislikeBtn;
    private JPanel recipeCard;
    private List<String[]> recipes;
    private int currentIndex; 

    public SwipeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0xF2, 0xEF, 0xEB)); // #f2efeb

        // === Dummy Recipe Data ===
        recipes = new ArrayList<>();
        recipes.add(new String[]{"Mediterranean Pasta Bowl", "Vegetarian • Low-Calorie"});
        recipes.add(new String[]{"Spicy Chicken Tacos", "High-Protein • Gluten-Free"});
        recipes.add(new String[]{"Avocado Toast", "Vegan • Low-Calorie"});
        recipes.add(new String[]{"Beef Stir-Fry", "High-Calorie • Protein-Rich"});
        currentIndex = 0;

        // ====================== Recipe Card Panel ======================
        //recipeCard = new RoundedPanel(25, Color.WHITE); 
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
        viewRecipeBtn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        viewRecipeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Hover Effect
        viewRecipeBtn.addMouseListener(new MouseAdapter() {
            
        }) 

        // ====================== Like / Dislike Buttons ======================

    }
}








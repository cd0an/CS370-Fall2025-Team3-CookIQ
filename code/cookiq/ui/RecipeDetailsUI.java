/**
 * RecipeDetailsUI.java
 *
 * Displays detailed information about a selected recipe:
 * ingredients, instructions, cost, cooking time, nutrition, and image.
 */

package cookiq.ui;

import cookiq.models.Recipe;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

public class RecipeDetailsUI extends JPanel {

    private MainFrame mainFrame;
    private Recipe recipe;
    private JSplitPane splitPane; // expose split pane to control divider if needed

    public RecipeDetailsUI(MainFrame mainFrame, Recipe recipe) {
        this.mainFrame = mainFrame;
        this.recipe = recipe;

        setLayout(new BorderLayout());
        setBackground(new Color(0xF2, 0xEF, 0xEB));

        // ===== Main Split Pane =====
        splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.35); // left panel gets 35% width now
        splitPane.setDividerLocation(0.35);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        // ===== Left Panel =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(0xF2, 0xEF, 0xEB));
        leftPanel.setMinimumSize(new Dimension(450, 0));

        // Increased top spacing
        leftPanel.add(Box.createVerticalStrut(100));

        // Placeholder image
        JLabel imageLabel = new JLabel("Recipe Image", SwingConstants.CENTER);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.LIGHT_GRAY);
        imageLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageLabel.setPreferredSize(new Dimension(500, 350));
        imageLabel.setMaximumSize(new Dimension(500, 350));
        leftPanel.add(imageLabel);
        leftPanel.add(Box.createVerticalStrut(20));

        // ===== Info boxes row =====
        JPanel infoRow = new JPanel(new GridLayout(1, 3, 15, 0)); // more gap between boxes
        infoRow.setBackground(new Color(0xF2, 0xEF, 0xEB));
        infoRow.setMaximumSize(new Dimension(500, 100)); // bigger boxes

        infoRow.add(createSmallInfoBox("Cuisine", recipe.getCuisine()));
        infoRow.add(createSmallInfoBox("Cook Time", recipe.getCookTime() + " min"));
        infoRow.add(createSmallInfoBox("Cost", String.format("$%.2f", recipe.getCost())));
        leftPanel.add(infoRow);

        leftPanel.add(Box.createVerticalStrut(30));

        // ===== Nutrition Panel =====
        JPanel nutritionPanel = new JPanel(new GridLayout(2, 1));
        nutritionPanel.setBackground(Color.WHITE);
        nutritionPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
                "Nutritional Facts",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        nutritionPanel.setMaximumSize(new Dimension(500, 180)); // bigger nutrition panel
        nutritionPanel.add(new JLabel("Calories: " + recipe.getCalories(), SwingConstants.CENTER));
        nutritionPanel.add(new JLabel("Protein: 35g", SwingConstants.CENTER));
        leftPanel.add(nutritionPanel);

        leftPanel.add(Box.createVerticalStrut(60)); // bottom spacing

        splitPane.setLeftComponent(leftPanel);

        // ===== Right Panel =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(0xF2, 0xEF, 0xEB));
        rightPanel.setMinimumSize(new Dimension(500, 0));

        // Recipe title
        JLabel titleLabel = new JLabel(recipe.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        rightPanel.add(titleLabel, BorderLayout.NORTH);

        // Content wrapper
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(new Color(0xF2, 0xEF, 0xEB));
        contentWrapper.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentWrapper.add(Box.createVerticalStrut(10));
        contentWrapper.add(createFixedWidthPanel(createTextAreaPanel("Ingredients", recipe.getIngredients(), 100), 800));
        contentWrapper.add(Box.createVerticalStrut(15));
        contentWrapper.add(createFixedWidthPanel(createTextAreaPanel("Instructions", recipe.getDirections(), 150), 800));
        contentWrapper.add(Box.createVerticalStrut(30));
        contentWrapper.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        splitPane.setRightComponent(rightPanel);
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    // ===== Helper Methods =====
    private JPanel createSmallInfoBox(String title, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
                title,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14)
        ));
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("SansSerif", Font.PLAIN, 16)); // slightly bigger font
        panel.add(valueLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTextAreaPanel(String title, List<String> lines, int height) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16)
        ));
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setBackground(Color.WHITE);

        StringBuilder sb = new StringBuilder();
        if (title.equals("Instructions")) {
            for (int i = 0; i < lines.size(); i++) {
                sb.append(i + 1).append(". ").append(lines.get(i)).append("\n\n");
            }
        } else {
            for (String line : lines) {
                sb.append("• ").append(line).append("\n");
            }
        }
        area.setText(sb.toString());

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(750, height));
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFixedWidthPanel(JPanel panel, int width) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.setBackground(new Color(0xF2, 0xEF, 0xEB));
        wrapper.add(Box.createHorizontalGlue());
        panel.setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
        wrapper.add(panel);
        wrapper.add(Box.createHorizontalGlue());
        return wrapper;
    }

    // ===== Temporary main method for testing =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Recipe Details UI Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

            Recipe dummyRecipe = new Recipe(
                    "1",
                    "Spaghetti Carbonara",
                    "Italian",
                    "Vegetarian",
                    30,
                    12.5,
                    420,
                    List.of("Spaghetti", "Eggs", "Parmesan Cheese", "Pepper"),
                    List.of("Boil pasta", "Mix eggs and cheese", "Combine with pasta", "Season with pepper"),
                    null
            );

            RecipeDetailsUI ui = new RecipeDetailsUI(null, dummyRecipe);
            frame.setContentPane(ui);
            frame.setVisible(true);
        });
    }
}




































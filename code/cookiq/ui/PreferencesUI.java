/**
 * PreferencesUI.java
 *
 * Displays questions for users to select dietary restrictions, health goals,
 * preferred cuisines, cooking time, budget, and available ingredients.
 */

package cookiq.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.bson.Document;

import cookiq.db.RecipeRepository;
import cookiq.models.Preferences;

public class PreferencesUI extends JPanel {

    private Preferences preferences;

    // ====================== Input Components ======================
    private JCheckBox vegetarianCB, ketoCB, glutenCB;
    private JCheckBox lowCalCB, highCalCB, highProteinCB;
    private JCheckBox italianCB, mexicanCB, asianCB, americanCB, medCB;
    private JRadioButton time15, time30, time60;
    private JRadioButton budget10, budget30, budget50;
    private JTextField ingredientField;

    public PreferencesUI() {
        preferences = new Preferences();
        setLayout(new BorderLayout());
        setBackground(new Color(0xF2, 0xEF, 0xEB)); // #f2efeb

        Color textColor = new Color(0x47, 0x3C, 0x38); // #473c38

        // ====================== Title ======================
        JLabel titleLabel = new JLabel("Set Your Preferences");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // ====================== White Panel for Preferences ======================
        JPanel whitePanel = new JPanel(); 
        whitePanel.setLayout(new BoxLayout(whitePanel, BoxLayout.Y_AXIS));
        whitePanel.setBackground(Color.WHITE);
        whitePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        whitePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        Font optionFont = new Font("SansSerif", Font.PLAIN, 16);
        Font borderFont = new Font("SansSerif", Font.BOLD, 16);

        // ============ Dietary Restrictions ============ 
        JPanel dietPanel = new JPanel(new GridLayout(0, 1));
        dietPanel.setBackground(Color.WHITE);
        dietPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
            "Dietary Restrictions (select all that apply):",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            borderFont
        ));

        // Checkboxes 
        vegetarianCB = new JCheckBox("Vegetarian"); 
        ketoCB = new JCheckBox("Keto"); 
        glutenCB = new JCheckBox("Gluten-free"); 
        JCheckBox[] dietBoxes = {vegetarianCB, ketoCB, glutenCB};
        for (JCheckBox box : dietBoxes) {
            box.setFont(optionFont);
            dietPanel.add(box);
        }
        whitePanel.add(dietPanel);
        whitePanel.add(Box.createRigidArea(new Dimension(0,10)));

        // ============ Health Goals ============
        JPanel healthPanel = new JPanel(new GridLayout(0, 1));
        healthPanel.setBackground(Color.WHITE);
        healthPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
            "Health Goals (select all that apply):",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            borderFont
        ));

        // Checkboxes 
        lowCalCB = new JCheckBox("Low-calorie"); 
        highCalCB = new JCheckBox("High-calorie"); 
        highProteinCB = new JCheckBox("High-protein"); 
        JCheckBox[] healthBoxes = {lowCalCB, highCalCB, highProteinCB};
        for (JCheckBox box : healthBoxes) {
            box.setFont(optionFont);
            healthPanel.add(box);
        }
        whitePanel.add(healthPanel);
        whitePanel.add(Box.createRigidArea(new Dimension(0,10)));

        // ============ Preferred Cuisines  ============
        JPanel cuisinesPanel = new JPanel(new GridLayout(0, 1));
        cuisinesPanel.setBackground(Color.WHITE);
        cuisinesPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
            "Preferred Cuisines (select all that apply):",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            borderFont
        ));

        // Checkboxes 
        italianCB = new JCheckBox("Italian"); 
        mexicanCB = new JCheckBox("Mexican"); 
        asianCB = new JCheckBox("Asian"); 
        americanCB = new JCheckBox("American"); 
        medCB = new JCheckBox("Mediterranean"); 
        JCheckBox[] cuisineBoxes = {italianCB, mexicanCB, asianCB, americanCB, medCB};
        for (JCheckBox box : cuisineBoxes) {
            box.setFont(optionFont);
            cuisinesPanel.add(box);
        }
        whitePanel.add(cuisinesPanel);
        whitePanel.add(Box.createRigidArea(new Dimension(0,10)));

        // ============ Cook Time ============
        JPanel cookTimePanel = new JPanel(new GridLayout(0, 1));
        cookTimePanel.setBackground(Color.WHITE);
        cookTimePanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
            "Maximum Cook Time (select one option):",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            borderFont
        ));

        // Cook Time Radio Button 
        ButtonGroup timeGroup = new ButtonGroup();
        time15 = new JRadioButton(">15 min");
        time30 = new JRadioButton(">30 min");
        time60 = new JRadioButton(">60 min");
        JRadioButton[] timeButtons = {time15, time30, time60};
        for (JRadioButton btn : timeButtons) {
            btn.setFont(optionFont);
            btn.setBackground(Color.WHITE);
            timeGroup.add(btn);
            cookTimePanel.add(btn);
        }
        whitePanel.add(cookTimePanel);
        whitePanel.add(Box.createRigidArea(new Dimension(0,10)));

        // ============ Budget Per Meal ============
        JPanel budgetPanel = new JPanel(new GridLayout(0, 1));
        budgetPanel.setBackground(Color.WHITE);
        budgetPanel .setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
            "Budget Per Meal (select one option):",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            borderFont
        ));

        // Budget Per Meal Radio Button 
        ButtonGroup budgetGroup = new ButtonGroup();
        budget10 = new JRadioButton(">$10");
        budget30 = new JRadioButton(">$30");
        budget50 = new JRadioButton(">$50");
        JRadioButton[] budgetButtons = {budget10, budget30, budget50};
        for (JRadioButton btn : budgetButtons) {
            btn.setFont(optionFont);
            btn.setBackground(Color.WHITE);
            budgetGroup.add(btn);
            budgetPanel.add(btn);
        }
        whitePanel.add(budgetPanel);
        whitePanel.add(Box.createRigidArea(new Dimension(0,10)));

        // ============ Available Ingredients ============
        JPanel ingredientPanel = new JPanel(new GridLayout(0, 1));
        ingredientPanel.setBackground(Color.WHITE);
        ingredientPanel.setBorder(BorderFactory.createTitledBorder(
            new LineBorder(new Color(0xA3, 0xC4, 0xA1), 1),
            "Input your available ingredients:",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            borderFont
        ));

        // Ingredients Input Textbox 
        ingredientField = new JTextField(); 
        ingredientField.setPreferredSize(new Dimension(200,30));
        ingredientField.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Set black border

        String placeholder = "Type here (e.g., eggs)";
        ingredientField.setText(placeholder);
        ingredientField.setForeground(Color.GRAY);

        ingredientField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (ingredientField.getText().equals(placeholder)) {
                    ingredientField.setText("");
                    ingredientField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (ingredientField.getText().isEmpty()) {
                    ingredientField.setText(placeholder);
                    ingredientField.setForeground(Color.GRAY); 
                }
            }
        }); 

        ingredientPanel.add(ingredientField);
        whitePanel.add(ingredientPanel);
        whitePanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // ============ Add White Panel ============
        whitePanel.setPreferredSize(new Dimension(700, 700));
        add(whitePanel, BorderLayout.CENTER);

        // ============ Generate Recipes Button ============
        JButton generateBtn = new JButton("Generate Recipes");
        generateBtn.setBackground(new Color(0x6E, 0x92, 0x77)); // #6e9277 
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setPreferredSize(new Dimension(800, 30));
        generateBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        generateBtn.setAlignmentX(CENTER_ALIGNMENT);

        generateBtn.setFocusPainted(true); 
        generateBtn.setBorderPainted(false);
        generateBtn.setContentAreaFilled(true); 
        generateBtn.setOpaque(true); 

        // Add hover effect for generate recipe button 
        generateBtn.addMouseListener(new MouseAdapter() {
            Color originalColor = generateBtn.getBackground();
            Dimension originalSize = generateBtn.getPreferredSize();

            @Override
            public void mouseEntered(MouseEvent e) {
                generateBtn.setBackground(new Color(0x5A, 0x7B, 0x63)); 
                generateBtn.setPreferredSize(new Dimension(originalSize.width + 10, originalSize.height));
                generateBtn.revalidate();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                generateBtn.setBackground(originalColor);
                generateBtn.setPreferredSize(originalSize);
                generateBtn.revalidate();
            }

        });

        // Connects Preference UI input to Preference object from Preference.java
        generateBtn.addActionListener(e -> {
            // Update preferences object
            preferences.setVegetarian(vegetarianCB.isSelected());
            preferences.setKeto(ketoCB.isSelected());
            preferences.setGlutenFree(glutenCB.isSelected());

            preferences.setLowCalorie(lowCalCB.isSelected());
            preferences.setHighCalorie(highCalCB.isSelected());
            preferences.setHighProtein(highProteinCB.isSelected());

            // Cook Time
            if (time15.isSelected()) preferences.setMaxCookTime(15);
            else if (time30.isSelected()) preferences.setMaxCookTime(30);
            else if(time60.isSelected()) preferences.setMaxCookTime(60);

            // Budget 
            if (budget10.isSelected()) preferences.setMaxBudget(10);
            else if (budget30.isSelected()) preferences.setMaxBudget(30);
            else if (budget50.isSelected()) preferences.setMaxBudget(50);

            // Ingredients 
            String text = ingredientField.getText();
            if (!text.equals(placeholder) && !text.isEmpty()) {
                String[] ingredients = text.split(",");
                for (String ing : ingredients) {
                    preferences.addAvailableIngredient(ing.trim());
                }
            }

            // ============ Save to MongoDB ============
            Document doc = new Document() 
                .append("vegetarian", preferences.isVegetarian())
                .append("keto", preferences.isKeto())
                .append("glutenFree", preferences.isGlutenFree())
                .append("lowCalorie", preferences.isLowCalorie())
                .append("highCalorie", preferences.isHighCalorie())
                .append("highProtein", preferences.isHighProtein())
                .append("maxCookTime", preferences.getMaxCookTime())
                .append("maxBudget", preferences.getMaxBudget())
                .append("ingredients", preferences.getAvailableIngredients());

            RecipeRepository.insertDocument("user_preferences", doc);

            // Confirmation message
            JOptionPane.showMessageDialog(null, "Preferences saved!");
        });

        // ============ Reset Button ============
        JButton resetBtn = new JButton("Reset");
        resetBtn.setBackground(new Color(0x7FAE85));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setPreferredSize(new Dimension(120, 30));
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 15));
        resetBtn.setFocusPainted(true);
        resetBtn.setBorderPainted(false);
        resetBtn.setContentAreaFilled(true);
        resetBtn.setOpaque(true);

        // Add hover effect for reset button
        resetBtn.addMouseListener(new MouseAdapter() {
            Color originalColor = resetBtn.getBackground();
            @Override
            public void mouseEntered(MouseEvent e) {
                resetBtn.setBackground(new Color(0xC9302C));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                resetBtn.setBackground(originalColor);
            }
        }); 

        // Action for Reset Button: clears all inputs
        resetBtn.addActionListener( e -> {
            // Uncheck all checkboxes
            vegetarianCB.setSelected(false);
            ketoCB.setSelected(false);
            glutenCB.setSelected(false);
            lowCalCB.setSelected(false);
            highCalCB.setSelected(false);
            highProteinCB.setSelected(false);
            italianCB.setSelected(false);
            mexicanCB.setSelected(false);
            asianCB.setSelected(false);
            americanCB.setSelected(false);
            medCB.setSelected(false);

            // Deselected radio buttons
            time15.setSelected(false);
            time30.setSelected(false);
            time60.setSelected(false);
            budget10.setSelected(false);
            budget30.setSelected(false);
            budget50.setSelected(false);

            // Clear ingredient field
            ingredientField.setText("Type here (e.g., eggs)");
            ingredientField.setForeground(Color.GRAY);

            JOptionPane.showMessageDialog(null, "Preferences reset!");
        });

        // ============ Button Panel ============
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0xF2, 0xEF, 0xEB)); // #f2efeb
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS)); 
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(generateBtn);
        btnPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        btnPanel.add(resetBtn);
        btnPanel.add(Box.createHorizontalGlue());
    }
}



































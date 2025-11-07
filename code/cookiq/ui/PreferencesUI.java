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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

import cookiq.models.Preferences;
import cookiq.models.User;
import cookiq.services.UserService;

public class PreferencesUI extends JPanel {
    private MainFrame mainFrame; 
    private User curr_user;

    // ====================== Input Components ======================
    private JCheckBox vegetarianCB, ketoCB, glutenCB;
    private JCheckBox lowCalCB, highCalCB, highProteinCB;
    private JCheckBox italianCB, mexicanCB, asianCB, americanCB, medCB;
    private JRadioButton time15, time30, time60;
    private JRadioButton budget10, budget30, budget50;
    private JTextField ingredientField;

    // Button groups for radio buttons
    private ButtonGroup timeGroup;
    private ButtonGroup budgetGroup;

    public PreferencesUI(MainFrame frame) {
        this.mainFrame = frame;
        this.curr_user = frame.getCurrentUser();

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
        timeGroup = new ButtonGroup();
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
        budgetGroup = new ButtonGroup();
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
        generateBtn.setPreferredSize(new Dimension(180, 40));
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
            User curr_user = mainFrame.getCurrentUser();

            if (curr_user == null) {
                JOptionPane.showMessageDialog(null, "Error: No user available.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Ensure guest has a Preferences object
            if (curr_user.getPreferences() == null) {
                curr_user.setPreferences(new Preferences());
            }

            boolean anyDiet = vegetarianCB.isSelected() || ketoCB.isSelected() || glutenCB.isSelected();
            boolean anyHealth = lowCalCB.isSelected() || highCalCB.isSelected() || highProteinCB.isSelected();
            boolean anyCuisine = italianCB.isSelected() || mexicanCB.isSelected() || asianCB.isSelected()
                                || americanCB.isSelected() || medCB.isSelected();
            boolean anyTime = time15.isSelected() || time30.isSelected() || time60.isSelected();
            boolean anyBudget = budget10.isSelected() || budget30.isSelected() || budget50.isSelected();
            boolean anyIngredient = !ingredientField.getText().trim().isEmpty() &&
                                    !ingredientField.getText().equals("Type here (e.g., eggs)");

             if (!anyDiet && !anyHealth && !anyCuisine && !anyTime && !anyBudget && !anyIngredient
                && !curr_user.getUsername().equals("Guest")) {
                JOptionPane.showMessageDialog(null, "Please select your preferences first!",
                        "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Preferences selectedPrefs = new Preferences(
                vegetarianCB.isSelected(), ketoCB.isSelected(), glutenCB.isSelected(),
                lowCalCB.isSelected(), highCalCB.isSelected(), highProteinCB.isSelected(),
                italianCB.isSelected(), mexicanCB.isSelected(), asianCB.isSelected(),
                americanCB.isSelected(), medCB.isSelected(),
                time15.isSelected() ? 15 : time30.isSelected() ? 30 : 60,
                (double)(budget10.isSelected() ? 10 : budget30.isSelected() ? 30 : 50),
                new ArrayList<>()
            );

            // Copy preferences into the current user
            curr_user.getPreferences().copyPrefs(selectedPrefs);

            // Update available ingredients
            curr_user.getPreferences().getAvailableIngredients().clear();
            String text = ingredientField.getText();
            if (!text.equals("Type here (e.g., eggs)") && !text.isEmpty()) {
                for (String ing : text.split(",")) {
                    curr_user.getPreferences().addAvailableIngredient(ing.trim());
                }
            }

            // Save preferences to DB only if not guest
            if (!curr_user.getUsername().equals("Guest")) {
                new UserService().saveUserPreferences(curr_user.getUsername(), curr_user.getPreferences());
            }

            // Switch to SwipeUI
            if (mainFrame != null) mainFrame.showSwipeUI();
        });


        // ============ Reset Button ============
        JButton resetBtn = new JButton("Reset");
        resetBtn.setBackground(new Color(0xD9, 0x7A, 0x7A));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setPreferredSize(new Dimension(120, 40));
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
            // First, check if anything is selected
            boolean anyDiet = vegetarianCB.isSelected() || ketoCB.isSelected() || glutenCB.isSelected();
            boolean anyHealth = lowCalCB.isSelected() || highCalCB.isSelected() || highProteinCB.isSelected();
            boolean anyCuisine = italianCB.isSelected() || mexicanCB.isSelected() || asianCB.isSelected() 
                                || americanCB.isSelected() || medCB.isSelected();
            boolean anyTime = time15.isSelected() || time30.isSelected() || time60.isSelected();
            boolean anyBudget = budget10.isSelected() || budget30.isSelected() || budget50.isSelected();
            boolean anyIngredient = !ingredientField.getText().trim().isEmpty() &&
                                    !ingredientField.getText().equals("Type here (e.g., eggs)");

            if (!anyDiet && !anyHealth && !anyCuisine && !anyTime && !anyBudget && !anyIngredient) {
                // Nothing to reset
                JOptionPane.showMessageDialog(this, "No preferences to reset.", 
                                            "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Clear Preferences object in memory
            if (curr_user != null) {
                curr_user.getPreferences().clearAll();
            }

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

            // Clear radio buttons
            timeGroup.clearSelection();
            budgetGroup.clearSelection();

            // Reset ingredient field
            ingredientField.setText("Type here (e.g., eggs)");
            ingredientField.setForeground(Color.GRAY);

            // Save cleared preferences to DB
            if (curr_user != null) {
                UserService userService = new UserService();
                userService.saveUserPreferences(curr_user.getUsername(), curr_user.getPreferences());
            }

            JOptionPane.showMessageDialog(this, "Preferences reset!");
        });

        // ============ Button Panel ============
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0xF2, 0xEF, 0xEB)); // #f2efeb

        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        btnPanel.add(resetBtn);
        btnPanel.add(generateBtn);

        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(btnPanel, BorderLayout.SOUTH);
    }

        public void loadPreferencesFromUser(User user) {
            if (user == null || user.getPreferences() == null) return;

            Preferences prefs = user.getPreferences();

            vegetarianCB.setSelected(prefs.isVegetarian());
            ketoCB.setSelected(prefs.isKeto());
            glutenCB.setSelected(prefs.isGlutenFree());

            lowCalCB.setSelected(prefs.isLowCalorie());
            highCalCB.setSelected(prefs.isHighCalorie());
            highProteinCB.setSelected(prefs.isHighProtein());

            italianCB.setSelected(prefs.isItalian());
            mexicanCB.setSelected(prefs.isMexican());
            asianCB.setSelected(prefs.isAsian());
            americanCB.setSelected(prefs.isAmerican());
            medCB.setSelected(prefs.isMediterranean());

            time15.setSelected(prefs.getMaxCookTime() == 15);
            time30.setSelected(prefs.getMaxCookTime() == 30);
            time60.setSelected(prefs.getMaxCookTime() == 60);

            budget10.setSelected(prefs.getMaxBudget() == 10);
            budget30.setSelected(prefs.getMaxBudget() == 30);
            budget50.setSelected(prefs.getMaxBudget() == 50);

            if (prefs.getAvailableIngredients() != null && !prefs.getAvailableIngredients().isEmpty()) {
                ingredientField.setText(String.join(", ", prefs.getAvailableIngredients()));
            } else {
                ingredientField.setText("Type here (e.g., eggs)");
            }
        }
    }


































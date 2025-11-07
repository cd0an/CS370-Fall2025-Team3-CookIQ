package cookiq.TestCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import cookiq.services.ImageService;

public class RecipeViewer {

    private JFrame frame;
    private JLabel imageLabel;
    private JButton nextButton;
    private List<BufferedImage> images;
    private int currentIndex = 0;

    public RecipeViewer(List<BufferedImage> images) {
        this.images = images;
        createUI();
        showImage(0);
    }

    private void createUI() {
        frame = new JFrame("Recipe Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(imageLabel, BorderLayout.CENTER);

        nextButton = new JButton("Next Recipe");
        frame.add(nextButton, BorderLayout.SOUTH);

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentIndex++;
                if (currentIndex >= images.size()) {
                    currentIndex = 0; // loop back to first image
                }
                showImage(currentIndex);
            }
        });

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void showImage(int index) {
        if (images == null || images.isEmpty()) return;

        BufferedImage img = images.get(index);
        imageLabel.setIcon(new ImageIcon(img));
    }

    // --- Main method to test ---
    public static void main(String[] args) {
        ImageService imgService = new ImageService();
        
        // Example: load multiple images
        List<BufferedImage> recipeImages = new ArrayList<>();
        recipeImages.addAll(imgService.getImage("Apple Pie"));
        recipeImages.addAll(imgService.getImage("Caprese Stuffed Avocados"));
        recipeImages.addAll(imgService.getImage("Chocolate Cake"));

        new RecipeViewer(recipeImages);
    }
}

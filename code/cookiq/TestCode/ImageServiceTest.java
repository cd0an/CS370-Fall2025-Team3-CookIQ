package cookiq.TestCode;

import cookiq.services.ImageService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageServiceTest {

    public static void main(String[] args) {

        ImageService imgService = new ImageService();
        String recipe_name = "Pizza";

        // Load images for a recipe
        List<BufferedImage> images = imgService.getImage(recipe_name);
        if (images == null || images.isEmpty()) {
            System.out.println("No images found.");
            return;
        }

        // JFrame main window
        JFrame bg = new JFrame("Main Window");
        bg.setSize(600, 800);
        bg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bg.setLayout(new GridBagLayout());

        // Get scaled image with blurred background (NOT displayImage)
        ImageIcon scaledIcon = imgService.getScaledImage(images.get(0), 500, 350);
        
        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setPreferredSize(new Dimension(500, 350));
        imageLabel.setMaximumSize(new Dimension(500, 350));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        bg.add(imageLabel, gbc);
        bg.setVisible(true);
    }
}
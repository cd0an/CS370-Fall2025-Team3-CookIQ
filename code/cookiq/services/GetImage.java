package cookiq.services;

import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GetImage {
    public JLabel getImage(String recipe_name, int width, int height)
    {
        ImageService imgService = new ImageService();
        recipe_name = "Vegetarian Lentil Curry";

        //Load images for a recipe
        List<BufferedImage> images = imgService.getImage(recipe_name);
        if(images == null || images.isEmpty()) 
        {
            System.out.println("No images found.");
            return new JLabel("Recipe Image");
        }

        //JFrame main window
        JFrame bg = new JFrame("Main Window");
        bg.setSize(600, 800);
        bg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bg.setLayout(new GridBagLayout());

        //Get scaled image with blurred background (NOT displayImage)
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

        return imageLabel;
    }
}

package cookiq.TestCode;

import cookiq.services.ImageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImageServiceTest {

    public static void main(String[] args) {
        ImageService imgService = new ImageService();

        // Load images for a recipe
        List<BufferedImage> images = imgService.getImage("Apple Pie");
        if (images == null || images.isEmpty()) {
            System.out.println("No images found.");
            return;
        }

        // JFrame setup
        JFrame frame = new JFrame("Recipe Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // JLabel for displaying images
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(imageLabel, BorderLayout.CENTER);

        // JButton to go to the next image
        JButton nextButton = new JButton("Next Image");
        frame.add(nextButton, BorderLayout.SOUTH);

        // Index to keep track of current image
        final int[] index = {0};

        // Display the first image
        imageLabel.setIcon(new ImageIcon(images.get(index[0]).getScaledInstance(500, 300, Image.SCALE_SMOOTH)));

        // Button click action
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                index[0]++;
                if (index[0] >= images.size()) index[0] = 0; // loop back
                BufferedImage img = images.get(index[0]);
                imageLabel.setIcon(new ImageIcon(img.getScaledInstance(500, 300, Image.SCALE_SMOOTH)));
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

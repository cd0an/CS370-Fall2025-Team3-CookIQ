package cookiq.models;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.List;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

/**
 * This class is used to allow the queried image to be its original size
 * yet still fill the frame of the JFrame/JLabel on where it's called.
 */
public class ImageBlurPanel extends JPanel {

    private BufferedImage image;

    public ImageBlurPanel(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // --- Step 1: Create blurred background ---
        BufferedImage blurred = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bg = blurred.createGraphics();
        // Stretch image to fill panel for background
        bg.drawImage(image, 0, 0, panelWidth, panelHeight, null);
        bg.dispose();

        // Apply a simple blur (box blur)
        float[] kernel = new float[9];
        for (int i = 0; i < 9; i++) kernel[i] = 1f / 9f;
        ConvolveOp op = new ConvolveOp(new Kernel(3, 3, kernel));
        blurred = op.filter(blurred, null);

        // Draw blurred background
        g2.drawImage(blurred, 0, 0, null);

        // --- Step 2: Draw the image at natural size, centered ---
        int x = (panelWidth - image.getWidth()) / 2;
        int y = (panelHeight - image.getHeight()) / 2;
        g2.drawImage(image, x, y, null);
    }

    // --- Utility method to display multiple images ---
    public static void displayImages(List<BufferedImage> images) {
        for (BufferedImage img : images) {
            JFrame frame = new JFrame("Image with Blurred Edges");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ImageBlurPanel panel = new ImageBlurPanel(img);
            frame.add(panel);

            frame.setSize(800, 600); // initial window size
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}

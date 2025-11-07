package cookiq.TestCode;

import java.net.URL;

import javax.swing.ImageIcon; //Container for the image
import javax.swing.JFrame; //Blank frame where you can put images
import javax.swing.JLabel; //Can display text and iamges in a gui

public class DisplayImageFromURL {
    public static void main(String[] args) {
        try {
            //Example image URL from your search
            String imageUrl = "https://www.closetcooking.com/wp-content/uploads/2015/08/CapreseStuffedAvocados8003907.jpg";
            URL url = new URL(imageUrl);

            //Load image into ImageIcon
            ImageIcon imageIcon = new ImageIcon(url);

            //Display in JLabel inside a JFrame
            JLabel label = new JLabel(imageIcon);
            JFrame frame = new JFrame("Image from URL");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(label);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

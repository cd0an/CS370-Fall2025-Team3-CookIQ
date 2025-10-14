package cookiq;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import cookiq.ui.SwipeUI;

public class SwipeUITest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwipeUI Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 700);  // Adjust window size as needed

            SwipeUI swipeUI = new SwipeUI();
            frame.setContentPane(swipeUI);

            frame.setLocationRelativeTo(null); // Center on screen
            frame.setVisible(true);
        });
    }
}


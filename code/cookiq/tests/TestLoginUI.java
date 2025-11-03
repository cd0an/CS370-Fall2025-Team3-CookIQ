package cookiq.tests;

import javax.swing.*;
import cookiq.ui.LoginUI;

public class TestLoginUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CookIQ - Test Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);
            frame.setLocationRelativeTo(null);

           
            frame.setContentPane(new LoginUI());
            frame.setVisible(true);
        });
    }
}

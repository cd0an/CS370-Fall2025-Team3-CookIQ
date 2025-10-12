/**
 * LoginUI.java 
 *
 * Handles the login screen for CookIQ.
 * Collects username and password and passes them to UserService.
 */

package cookiq.ui;

import java.util.Scanner;

import cookiq.models.User;
import cookiq.services.UserService;

public class LoginUI {
    private UserService userService;
    private Scanner scanner;

    public LoginUI() {
        userService = new UserService(); // load users
        scanner = new Scanner(System.in);
    }

    // Start menu
    public void start() {
        System.out.println("=== Welcome to CookIq ===");
        while (true) {
            System.out.println("\n1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                handleRegister();
            } else if (choice.equals("2")) {
                handleLogin();
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Invalid choice, try again.");
            }
        }
    }

    // Handle user registration
    private void handleRegister() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (userService.registerUser(username, password)) {
            System.out.println(" Registration successful!");
        } else {
            System.out.println(" Username already taken.");
        }
    }

    // Handle user login
    private void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = userService.loginUser(username, password);
        if (user != null) {
            System.out.println(" Login successful! Welcome, " + user.getUsername());
            // Later: open PreferencesUI here
        } else {
            System.out.println(" Invalid username or password.");
        }
    }
}


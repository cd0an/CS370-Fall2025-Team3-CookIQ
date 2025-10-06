/**
 * CookIQ.java
 *
 * Main entry point for the CookIQ personalized meal recommendation system.
 * 
 * This program allows users to input their dietary restrictions, health goals,
 * preferred cuisines, cooking methods, available ingredients, and budget.
 * Based on these preferences, CookIQ filters and ranks recipes from a dataset,
 * presenting the best-matching options in a swipe/like-dislike interface.
 * 
 * CookIQ.java initializes the application, launches the UI, and coordinates
 * between the user interface, business logic (services), and data models.
 *
 */

import cookiq.ui.LoginUI;

public class CookIQ {
    public static void main(String[] args) {
        LoginUI loginUI = new LoginUI();
        loginUI.start();
    }
}

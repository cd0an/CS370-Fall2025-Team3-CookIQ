/**
 * Tests saving, resetting, and reloading of user preferences.
 */

package cookiq.TestCode;

import java.util.Arrays;

import cookiq.models.Preferences;
import cookiq.services.UserService;

public class TestLoadPreferences {
    public static void main(String[] args) {
        // Create a UserService instance
        UserService userService = new UserService();
        String username = "cindy";

        // Step 1: Create and save preferences for this user
        Preferences prefs = new Preferences(
            true,  // vegetarian
            true,  // keto
            false, // glutenFree
            true,  // lowCalorie
            false, // highCalorie
            false, // highProtein
            true,  // italian
            false, // mexican
            false, // asian
            false, // american
            false, // mediterranean
            30,    // maxCookTime
            25.0,  // maxBudget
            Arrays.asList("tomatoes", "cheese", "basil")
        );

        userService.saveUserPreferences(username, prefs);
        System.out.println("✅ Saved initial preferences for " + username);

        // Step 2: Load and print preferences before reset
        System.out.println("\n=== Preferences BEFORE Reset ===");
        Preferences beforeReset = userService.getUserPreferences(username);
        userService.printPreferences(beforeReset);

        // Step 3: Simulate clearing preferences (like clicking reset in UI)
        beforeReset.clearAll();
        userService.saveUserPreferences(username, beforeReset);
        System.out.println("\n✅ Preferences cleared and saved.");

        // Step 4: Load and print preferences after reset
        System.out.println("\n=== Preferences AFTER Reset ===");
        Preferences afterReset = userService.getUserPreferences(username);
        userService.printPreferences(afterReset);

        // Step 5: Verify the reset worked
        if (afterReset.isEmpty()) {
            System.out.println("\n✅ Test passed: Preferences were successfully cleared.");
        } else {
            System.out.println("\n❌ Test failed: Some preferences were not cleared properly.");
        }
    }
}

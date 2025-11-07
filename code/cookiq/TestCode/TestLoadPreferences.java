/**
 * Tests saving, resetting, and reloading of user preferences.
 */

package cookiq.TestCode;

import java.util.Arrays;

import cookiq.models.Preferences;
import cookiq.services.UserService;

public class TestLoadPreferences {
    public static void main(String[] args) {
        UserService userService = new UserService();
        String username = "cindy";

        // STEP 1: Create and save initial preferences
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

        System.out.println("\n=== Preferences BEFORE Reset ===");
        Preferences beforeReset = userService.getUserPreferences(username);
        userService.printPreferences(beforeReset);

        // STEP 2: Simulate Reset (like clicking reset in UI)
        beforeReset.clearAll();
        userService.saveUserPreferences(username, beforeReset);
        System.out.println("\n✅ Preferences cleared and saved.");

        System.out.println("\n=== Preferences AFTER Reset ===");
        Preferences afterReset = userService.getUserPreferences(username);
        userService.printPreferences(afterReset);

        if (afterReset.isEmpty()) {
            System.out.println("\n✅ Test passed: Preferences were successfully cleared.");
        } else {
            System.out.println("\n❌ Test failed: Some preferences were not cleared properly.");
        }

        // STEP 3: Save new preferences again (simulate user setting them after reset)
        Preferences newPrefs = new Preferences(
            false, // vegetarian
            false, // keto
            true,  // glutenFree
            false, // lowCalorie
            true,  // highCalorie
            true,  // highProtein
            false, // italian
            true,  // mexican
            true,  // asian
            false, // american
            true,  // mediterranean
            60,    // maxCookTime
            40.0,  // maxBudget
            Arrays.asList("chicken", "rice", "onions")
        );

        userService.saveUserPreferences(username, newPrefs);
        System.out.println("\n✅ Saved new preferences after reset.");

        // STEP 4: Reload and print to verify new data persisted
        System.out.println("\n=== Preferences AFTER Setting New Ones ===");
        Preferences reloaded = userService.getUserPreferences(username);
        userService.printPreferences(reloaded);
    }
}


/**
 * Preferences.java
 *
 * Stores a user's selected dietary restrictions, health goals, cuisines,
 * cooking preferences, budget, and available ingredients.
 */

package cookiq.models;

public class Preferences {  
    private String dietaryPreference;
    private String preferredCuisine;
    private int maxCookTime;
    
    public Preferences(String dietaryPreference, String preferredCuisine, int maxCookTime) {
        this.dietaryPreference = dietaryPreference;
        this.preferredCuisine = preferredCuisine;
        this.maxCookTime = maxCookTime;
    }
    
    public String getDietaryPreference() { return dietaryPreference; }
    public String getPreferredCuisine() { return preferredCuisine; }
    public int getMaxCookTime() { return maxCookTime; }
}
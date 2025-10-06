/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 */

package services;

import models.Preferences;
import models.Recipe;
import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
    private List<Recipe> recipeDatabase;
    
    public RecommendationService() {
        this.recipeDatabase = new ArrayList<>();
        addTestRecipes();
    }
    
    public List<Recipe> getRecommendations(Preferences preferences) {
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipeDatabase) {
            if (matchesPreferences(recipe, preferences)) {
                matches.add(recipe);
            }
        }
        return matches;
    }
    
    private boolean matchesPreferences(Recipe recipe, Preferences prefs) {
        if (!recipe.getDietaryCategory().equals(prefs.getDietaryPreference())) return false;
        if (!recipe.getCuisine().equals(prefs.getPreferredCuisine())) return false;
        if (recipe.getCookTime() > prefs.getMaxCookTime()) return false;
        return true;
    }
    
    private void addTestRecipes() {
        recipeDatabase.add(new Recipe("Kebab", "middle eastern", "keto", 30));
        recipeDatabase.add(new Recipe("Salad", "middle eastern", "keto", 15));
        recipeDatabase.add(new Recipe("Pasta", "italian", "vegetarian", 20));
    }
}
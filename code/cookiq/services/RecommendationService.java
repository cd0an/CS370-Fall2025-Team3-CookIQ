/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 */

package cookiq.services;

import java.util.ArrayList;
import java.util.List;

import cookiq.models.Preferences;
import cookiq.models.Recipe;

public class RecommendationService {
    private List<Recipe> recipeDatabase;
    
    public RecommendationService() {
        this.recipeDatabase = new ArrayList<>();
    }
    
    public List<Recipe> getRecommendations(Preferences preferences) {
        List<Recipe> matches = new ArrayList<>();
        
        for (Recipe recipe : recipeDatabase) {
            if (matchesAllPreferences(recipe, preferences)) {
                matches.add(recipe);
            }
        }
        
        return matches;
    }
    
    private boolean matchesAllPreferences(Recipe recipe, Preferences prefs) {
        if (!matchesDietaryRestrictions(recipe, prefs)) {
            return false;
        }
        
        if (!matchesHealthGoals(recipe, prefs)) {
            return false;
        }
        
        if (!matchesCuisinePreferences(recipe, prefs)) {
            return false;
        }
        
        if (prefs.getMaxCookTime() > 0 && recipe.getCookTime() > prefs.getMaxCookTime()) {
            return false;
        }
        
        if (prefs.getMaxBudget() > 0 && recipe.getCost() > prefs.getMaxBudget()) {
            return false;
        }
        
        return true;
    }
    
    private boolean matchesDietaryRestrictions(Recipe recipe, Preferences prefs) {
        String dietaryCategory = recipe.getDietaryCategory().toLowerCase();
        
        if (prefs.isVegetarian() && !dietaryCategory.contains("vegetarian")) {
            return false;
        }
        
        if (prefs.isKeto() && !dietaryCategory.contains("keto")) {
            return false;
        }
        
        if (prefs.isGlutenFree() && !dietaryCategory.contains("gluten-free")) {
            return false;
        }
        
        return true;
    }
    
    private boolean matchesHealthGoals(Recipe recipe, Preferences prefs) {
        int calories = recipe.getCalories();
        
        if (prefs.isLowCalorie() && calories > 400) {
            return false;
        }
        
        if (prefs.isHighCalorie() && calories < 600) {
            return false;
        }
        
        if (prefs.isHighProtein() && calories < 500) {
            return false;
        }
        
        return true;
    }
    
    private boolean matchesCuisinePreferences(Recipe recipe, Preferences prefs) {
        String cuisine = recipe.getCuisine().toLowerCase();
        
        if (!prefs.isItalian() && !prefs.isMexican() && !prefs.isAsian() && 
            !prefs.isAmerican() && !prefs.isMediterranean()) {
            return true;
        }
        
        if (prefs.isItalian() && cuisine.contains("italian")) {
            return true;
        }
        if (prefs.isMexican() && cuisine.contains("mexican")) {
            return true;
        }
        if (prefs.isAsian() && (cuisine.contains("asian") || cuisine.contains("chinese"))) {
            return true;
        }
        if (prefs.isAmerican() && cuisine.contains("american")) {
            return true;
        }
        if (prefs.isMediterranean() && cuisine.contains("mediterranean")) {
            return true;
        }
        
        return false;
    }
    
    public void setRecipeDatabase(List<Recipe> recipes) {
        this.recipeDatabase = new ArrayList<>(recipes);
    }
}
/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 */

package cookiq.services;

import cookiq.models.Preferences;
import cookiq.models.Recipe;
import java.util.ArrayList;
import java.util.List;

public class RecommendationService {
    private List<Recipe> recipeDatabase;
    
    public RecommendationService() {
        this.recipeDatabase = new ArrayList<>();
        // addTestRecipes();❗Commented this out 
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
        // Check dietary restrictions
        if (!matchesDietaryRestrictions(recipe, prefs)) {
            return false;
        }
        
        // Check health goals
        if (!matchesHealthGoals(recipe, prefs)) {
            return false;
        }
        
        // Check cuisine preferences
        if (!matchesCuisinePreferences(recipe, prefs)) {
            return false;
        }
        
        // Check cooking time
        if (prefs.getMaxCookTime() > 0 && recipe.getCookTime() > prefs.getMaxCookTime()) {
            return false;
        }
        
        // Check budget
        if (prefs.getMaxBudget() > 0 && recipe.getCost() > prefs.getMaxBudget()) {
            return false;
        }
        
        return true;
    }
    
    private boolean matchesDietaryRestrictions(Recipe recipe, Preferences prefs) {
        String dietaryCategory = recipe.getDietaryCategory().toLowerCase();
        
        // If vegetarian is selected, recipe must be vegetarian
        if (prefs.isVegetarian() && !dietaryCategory.contains("vegetarian")) {
            return false;
        }
        
        // If keto is selected, recipe must be keto
        if (prefs.isKeto() && !dietaryCategory.contains("keto")) {
            return false;
        }
        
        // If gluten-free is selected, recipe must be gluten-free
        if (prefs.isGlutenFree() && !dietaryCategory.contains("gluten-free")) {
            return false;
        }
        
        return true;
    }
    
    private boolean matchesHealthGoals(Recipe recipe, Preferences prefs) {
        int calories = recipe.getCalories();
        
        // Check low-calorie preference
        if (prefs.isLowCalorie() && calories > 400) {
            return false;
        }
        
        // Check high-calorie preference
        if (prefs.isHighCalorie() && calories < 600) {
            return false;
        }
        
        // Check high-protein preference (simplified - assume high protein if calories > 500)
        if (prefs.isHighProtein() && calories < 500) {
            return false;
        }
        
        return true;
    }
    
    private boolean matchesCuisinePreferences(Recipe recipe, Preferences prefs) {
        String cuisine = recipe.getCuisine().toLowerCase();
        
        // If no cuisine preference is selected, match all cuisines
        if (!prefs.isItalian() && !prefs.isMexican() && !prefs.isAsian() && 
            !prefs.isAmerican() && !prefs.isMediterranean()) {
            return true;
        }
        
        // Check specific cuisine preferences
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
    
    //❗Add this back in if it's important❗
    // private void addTestRecipes() {
    //     // Add recipes with complete data including cost and calories
    //     recipeDatabase.add(new Recipe("Caprese Stuffed Avocados", "italian", "vegetarian", 10, 12.50, 320));
    //     recipeDatabase.add(new Recipe("Keto Bacon Cheeseburger Casserole", "american", "keto", 35, 18.75, 650));
    //     recipeDatabase.add(new Recipe("Gluten-Free Chicken Stir Fry", "asian", "gluten-free", 20, 15.25, 420));
    //     recipeDatabase.add(new Recipe("Vegetarian Lentil Curry", "asian", "vegetarian", 35, 8.99, 380));
    //     recipeDatabase.add(new Recipe("Keto Garlic Butter Salmon", "american", "keto", 15, 22.50, 580));
    //     recipeDatabase.add(new Recipe("Gluten-Free Quinoa Salad", "mediterranean", "gluten-free", 25, 9.75, 280));
    //     recipeDatabase.add(new Recipe("Mushroom and Spinach Quesadillas", "mexican", "vegetarian", 12, 7.50, 350));
    // }
}
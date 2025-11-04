/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 * 
 * ❗❗❗--> Implement a secondary list for the next closest recipe results where 4/5 preferences match.
 */


package cookiq.services;

import cookiq.models.Preferences; //Used for getters for cook time, cost, max budget
import cookiq.models.Recipe;

import java.util.ArrayList; //ArrayList class for a resizeable array
import java.util.List; //Defines all behaviors that any list like DSA should have

public class RecommendationService {
    private List<Recipe> recipeDatabase;
    private static final int perfectMatch = 5;
    private static final int semiMatch = 4;
    
    public RecommendationService() {
        this.recipeDatabase = new ArrayList<>();
    }
    
    public List<Recipe> getRecommendations(Preferences preferences) {
        List<Recipe> matches = new ArrayList<>();
        
        for (Recipe recipe : recipeDatabase) {
            // ❗Do not delete❗
            // if (matchesAllPreferences(recipe, preferences) == perfectMatch) {
            //     matches.add(recipe);
            // }

            if (matchesAllPreferences(recipe, preferences)) {
                matches.add(recipe);
            }
        }
        
        return matches;
    }
    
    // ❗Do not delete❗
    // private int matchesAllPreferences(Recipe recipe, Preferences prefs) {
    //     int count = 0; //Counter for number of satisfied preferences
    //     count += (matchesDietaryRestrictions(recipe, prefs)) ? 1 : 0;
    //     count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
    //     count += (matchesCuisinePreferences(recipe, prefs)) ? 1 : 0;
    //     count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
    //     count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
    //     count += (prefs.getMaxCookTime() > 0 && recipe.getCookTime() > prefs.getMaxCookTime()) ? 1 : 0;
    //     count += (prefs.getMaxBudget() > 0 && recipe.getCost() > prefs.getMaxBudget()) ? 1 : 0;

    //     //getCookTime() --> returns the cook time of the recipe.
    //     //getMaxCookTime() --> returns the willingness to wait selected in the users preference
    //     //❗This could probably be better implemented, implement ranges maybe?
    //     // if(prefs.getMaxCookTime() > 0 && recipe.getCookTime() > prefs.getMaxCookTime()) { return false; }
        
    //     //getCost() --> returns the cost of the recipe
    //     //getMaxBudget() --> returns the willingness to pay selected in the users preference
    //     //❗This could probably be better implemented, implement ranges maybe?
    //     // if(prefs.getMaxBudget() > 0 && recipe.getCost() > prefs.getMaxBudget()) { return false; }
        
    //     return count;
    // }

    private boolean matchesAllPreferences(Recipe recipe, Preferences prefs) {
        int count = 0; //Counter for number of satisfied preferences
        count += (matchesDietaryRestrictions(recipe, prefs)) ? 1 : 0;
        if(!matchesDietaryRestrictions(recipe, prefs)) { return false; }

        count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
        if(!matchesHealthGoals(recipe, prefs)) { return false; }
        
        count += (matchesCuisinePreferences(recipe, prefs)) ? 1 : 0;
        if(!matchesCuisinePreferences(recipe, prefs)) { return false; }

        count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
        if(!matchesHealthGoals(recipe, prefs)) { return false; }

        count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
        if(!matchesHealthGoals(recipe, prefs)) { return false; }
        
        //getCookTime() --> returns the cook time of the recipe.
        //getMaxCookTime() --> returns the willingness to wait selected in the users preference
        //❗This could probably be better implemented, implement ranges maybe?
        if(prefs.getMaxCookTime() > 0 && recipe.getCookTime() > prefs.getMaxCookTime()) { return false; }
        
        //getCost() --> returns the cost of the recipe
        //getMaxBudget() --> returns the willingness to pay selected in the users preference
        //❗This could probably be better implemented, implement ranges maybe?
        if(prefs.getMaxBudget() > 0 && recipe.getCost() > prefs.getMaxBudget()) { return false; }
        
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
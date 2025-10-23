/**
 * RecipeRanker.java
 *
 * Contains helper methods to score and rank recipes based on
 * how well they match user preferences.
 */
package cookiq.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cookiq.models.Preferences;
import cookiq.models.Recipe;

public class RecommendationService {
    private List<Recipe> recipeDatabase;
    
    public RecommendationService() {
        this.recipeDatabase = new ArrayList<>();
    }
    
    /**
     * Get recipe recommendations sorted by match score
     * Recipes are ranked based on how well they match user preferences
     */
    public List<Recipe> getRecommendations(Preferences preferences) {
        List<ScoredRecipe> scoredRecipes = new ArrayList<>();
        
        for (Recipe recipe : recipeDatabase) {
            int score = calculateMatchScore(recipe, preferences);
            scoredRecipes.add(new ScoredRecipe(recipe, score));
        }
        
        // Sort recipes by score in descending order (best matches first)
        scoredRecipes.sort(Comparator.comparing(ScoredRecipe::getScore).reversed());
        
        List<Recipe> recommendations = new ArrayList<>();
        for (ScoredRecipe scored : scoredRecipes) {
            recommendations.add(scored.getRecipe());
        }
        
        return recommendations;
    }
    
    /**
     * Calculate how well a recipe matches user preferences
     * Higher scores indicate better matches
     */
    private int calculateMatchScore(Recipe recipe, Preferences prefs) {
        int score = 0;
        
        // Dietary restrictions are highest priority - must match if selected
        String dietaryCategory = recipe.getDietaryCategory().toLowerCase();
        
        // Award points for matching dietary restrictions
        if (prefs.isVegetarian() && dietaryCategory.contains("vegetarian")) {
            score += 25;
        }
        if (prefs.isKeto() && dietaryCategory.contains("keto")) {
            score += 25;
        }
        if (prefs.isGlutenFree() && dietaryCategory.contains("gluten-free")) {
            score += 25;
        }
        
        // Heavy penalty if recipe doesn't match selected dietary restrictions
        boolean hasDietaryPreference = prefs.isVegetarian() || prefs.isKeto() || prefs.isGlutenFree();
        boolean matchesDietary = (prefs.isVegetarian() && dietaryCategory.contains("vegetarian")) ||
                                (prefs.isKeto() && dietaryCategory.contains("keto")) ||
                                (prefs.isGlutenFree() && dietaryCategory.contains("gluten-free"));
        
        if (hasDietaryPreference && !matchesDietary) {
            score -= 40;
        }
        
        // Cuisine preferences - medium priority
        String cuisine = recipe.getCuisine().toLowerCase();
        
        if (prefs.isItalian() && cuisine.contains("italian")) {
            score += 15;
        }
        if (prefs.isMexican() && cuisine.contains("mexican")) {
            score += 15;
        }
        if (prefs.isAsian() && (cuisine.contains("asian") || cuisine.contains("chinese"))) {
            score += 15;
        }
        if (prefs.isAmerican() && cuisine.contains("american")) {
            score += 15;
        }
        if (prefs.isMediterranean() && cuisine.contains("mediterranean")) {
            score += 15;
        }
        
        // Health goals - medium priority
        int calories = recipe.getCalories();
        
        if (prefs.isLowCalorie() && calories <= 400) {
            score += 12;
        }
        if (prefs.isHighCalorie() && calories >= 600) {
            score += 12;
        }
        if (prefs.isHighProtein() && calories >= 500) {
            score += 12;
        }
        
        // Cooking time - recipes within time limit get full points
        if (prefs.getMaxCookTime() > 0) {
            if (recipe.getCookTime() <= prefs.getMaxCookTime()) {
                score += 8;
            } else {
                // Gradual penalty for exceeding time limit
                int timeOver = recipe.getCookTime() - prefs.getMaxCookTime();
                int timePenalty = Math.min(timeOver / 5, 12);
                score -= timePenalty;
            }
        }
        
        // Budget - recipes within budget get full points, cheaper recipes are equal
        if (prefs.getMaxBudget() > 0) {
            if (recipe.getCost() <= prefs.getMaxBudget()) {
                score += 6;
            } else {
                // Penalty for exceeding budget
                double costOver = recipe.getCost() - prefs.getMaxBudget();
                int costPenalty = Math.min((int)(costOver / 2), 8);
                score -= costPenalty;
            }
        }
        
        // Ingredient matching - small bonus for using available ingredients
        if (!prefs.getAvailableIngredients().isEmpty()) {
            int ingredientMatches = countIngredientMatches(recipe, prefs.getAvailableIngredients());
            score += ingredientMatches * 2;
        }
        
        return Math.max(score, 0);
    }
    
    /**
     * Count how many recipe ingredients match user's available ingredients
     */
    private int countIngredientMatches(Recipe recipe, List<String> availableIngredients) {
        int matches = 0;
        List<String> recipeIngredients = recipe.getNER();
        
        if (recipeIngredients != null) {
            for (String ingredient : recipeIngredients) {
                String cleanIngredient = ingredient.toLowerCase().trim();
                for (String available : availableIngredients) {
                    if (cleanIngredient.contains(available)) {
                        matches++;
                        break;
                    }
                }
            }
        }
        
        return matches;
    }
    
    /**
     * Helper class to associate recipes with their match scores
     */
    private static class ScoredRecipe {
        private Recipe recipe;
        private int score;
        
        public ScoredRecipe(Recipe recipe, int score) {
            this.recipe = recipe;
            this.score = score;
        }
        
        public Recipe getRecipe() { return recipe; }
        public int getScore() { return score; }
    }
    
    /**
     * Set the recipe database from external source
     */
    public void setRecipeDatabase(List<Recipe> recipes) {
        this.recipeDatabase = new ArrayList<>(recipes);
    }
}
/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 * 
 * ❗❗❗--> Implement a secondary list for the next closest recipe results where 4/5 preferences match.
 */


package cookiq.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cookiq.db.RecipeRepository;
import cookiq.models.Preferences;
import cookiq.models.Recipe;
import cookiq.models.User;
import cookiq.utils.RecipeRanker;

/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 * 
 * UPDATED: Now uses real MongoDB data instead of dummy recipes
 * UPDATED: Dietary restrictions and health goals are mandatory filters
 * UPDATED: Uses RecipeRanker for scoring remaining preferences
 */
public class RecommendationService {
    private List<Recipe> allRecipes; // Now stores real recipes from MongoDB
    private RecipeRepository recipeRepository; // Handles MongoDB connection
    private RecipeRanker recipeRanker; // Handles scoring and ranking
    
    public RecommendationService() {
        this.recipeRepository = new RecipeRepository();
        this.recipeRanker = new RecipeRanker();
        loadRecipesFromMongoDB(); // Load real data on startup
    }

    // Fetch a single recipe by its name
    public Recipe getRecipeByName(String name) {
        if (name == null || allRecipes == null) return null;

        for (Recipe recipe : allRecipes) {
            if (recipe.getName() != null && recipe.getName().equalsIgnoreCase(name)) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * Load recipes from MongoDB instead of using dummy data
     */
    private void loadRecipesFromMongoDB() {
        this.allRecipes = recipeRepository.getAllRecipes();
        this.recipeRanker.setRecipeDatabase(this.allRecipes);
        System.out.println("Loaded " + this.allRecipes.size() + " recipes from MongoDB");
    }
    
    /**
     * Get recommendations with mandatory dietary and health goal filtering
     * then ranked by other preferences
     */
    public List<Recipe> getRecommendations(Preferences preferences, User user) {
        List<Recipe> matches = new ArrayList<>();
        
        // Step 1: Mandatory filtering for dietary restrictions and health goals
        List<Recipe> mandatoryFiltered = filterByMandatoryPreferences(preferences);
        
        if (mandatoryFiltered.isEmpty()) {
            System.out.println("No recipes match mandatory dietary/health filters");
            return matches; // Return empty list if no matches
        }
        
        // Step 2: Use RecipeRanker to score and rank the remaining recipes
        recipeRanker.setRecipeDatabase(mandatoryFiltered);
        List<Recipe> rankedRecommendations = recipeRanker.getRecommendations(preferences);

        // Step 3: Remove recipes already liked by the user
        if (user != null) {
            List<String> likedRecipeIds = user.getLikedRecipes();
            List<String> dislikedRecipeIds = user.getDislikedRecipes();
            rankedRecommendations.removeIf(recipe -> likedRecipeIds.contains(recipe.getId())
                                                    || dislikedRecipeIds.contains(recipe.getId()));
        }

        // Step 4: Limit to top 3 results
        Collections.shuffle(rankedRecommendations);
        int limit = Math.min(3, rankedRecommendations.size());
        List<Recipe> topRecommendations = rankedRecommendations.subList(0, limit);
        
        System.out.println("Returning " + rankedRecommendations.size() + " ranked recommendations");
        return topRecommendations;
    }
    
    /**
     * MANDATORY FILTERING: Recipes MUST match dietary restrictions AND health goals
     * This replaces the old matchesAllPreferences logic
     */
    private List<Recipe> filterByMandatoryPreferences(Preferences preferences) {
        List<Recipe> filtered = new ArrayList<>();
        
        for (Recipe recipe : allRecipes) {
            if (matchesDietaryRestrictions(recipe, preferences) && 
                matchesHealthGoals(recipe, preferences)) {
                filtered.add(recipe);
            }
        }
        
        System.out.println("After mandatory filtering: " + filtered.size() + " recipes (from " + 
                          allRecipes.size() + " total)");
        return filtered;
    }
    
    /**
     * Dietary restrictions are MANDATORY - recipe must match user's selections
     * UPDATED: Uses health_goals field from MongoDB instead of calories
     */
    private boolean matchesDietaryRestrictions(Recipe recipe, Preferences prefs) {
        String dietaryCategory = recipe.getDietaryCategory().toLowerCase();
        
        // If user has NO dietary restrictions selected, allow all recipes
        boolean hasDietaryPreference = prefs.isVegetarian() || prefs.isKeto() || prefs.isGlutenFree();
        
        if (!hasDietaryPreference) return true; // No dietary restrictions specified by user
        
        // User HAS dietary restrictions - recipe MUST match at least one
        if (prefs.isVegetarian() && dietaryCategory.contains("vegetarian")) return true;
        if (prefs.isKeto() && dietaryCategory.contains("keto")) return true;
        if (prefs.isGlutenFree() && dietaryCategory.contains("gluten-free")) return true;
        
        return false;
    }
    
    /**
     * Health goals are MANDATORY - recipe must match user's selections  
     * UPDATED: Uses health_goals field from MongoDB instead of calories
     */
    private boolean matchesHealthGoals(Recipe recipe, Preferences prefs) {
        String recipeHealth = recipe.getHealthGoals().toLowerCase();
        
        // If user has NO health goals selected, allow all recipes
        boolean hasHealthPreference = prefs.isLowCalorie() || prefs.isHighCalorie() || prefs.isHighProtein();
        
        if (!hasHealthPreference) return true; // No health goals specified by user
        
        // User HAS health goals - recipe MUST match at least one
        if (prefs.isLowCalorie() && recipeHealth.contains("low-calorie")) return true;
        if (prefs.isHighCalorie() && recipeHealth.contains("high-calorie")) return true;
        if (prefs.isHighProtein() && recipeHealth.contains("high-protein")) return true;
        
        return false;
    }
    
    /**
     * Refresh recipes from MongoDB (useful for "request new suggestions")
     */
    public void refreshRecipes() {
        loadRecipesFromMongoDB();
    }
    
    /**
     * Set recipe database (kept for compatibility with existing code)
     */
    public void setRecipeDatabase(List<Recipe> recipes) {
        this.allRecipes = new ArrayList<>(recipes);
        this.recipeRanker.setRecipeDatabase(this.allRecipes);
    }
    
    /**
     * Get all recipes without filtering (for debugging or admin use)
     */
    public List<Recipe> getAllRecipes() {
        return new ArrayList<>(allRecipes);
    }
    
    /**
     * Get count of total recipes loaded from MongoDB
     */
    public int getTotalRecipeCount() {
        return allRecipes.size();
    }
}
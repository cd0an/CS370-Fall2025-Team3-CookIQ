/**
 * RecommendationService.java
 *
 * Implements logic to rank recipes based on user preferences
 * and provide the best recommendations.
 * 
 * ❗❗❗--> Implement a secondary list for the next closest recipe results where 4/5 preferences match.
 */


// package cookiq.services;

// import java.util.ArrayList; //Used for getters for cook time, cost, max budget
// import java.util.List;

// import cookiq.models.Preferences; //ArrayList class for a resizeable array
// import cookiq.models.Recipe; //Defines all behaviors that any list like DSA should have

// public class V2_RecServ {
//     private List<Recipe> recipeDatabase;
//     private static final int PERFECT_MATCH = 6;
//     private static final int SEMI_MATCH = 5;
    
//     public V2_RecServ() {
//         this.recipeDatabase = new ArrayList<>();
//     }
    
//     /**
//      * Returns two lists of recipes
//      * 1) Perfect matches based on user preferences - 100% match
//      * 2) Semi matches based on user preferences - 83.3333% match
//      */
//     public List<List<Recipe>> getRecommendations(Preferences preferences) {
//         List<List<Recipe>> recipeContainer = new ArrayList<>();
//         List<Recipe> perfMatches = new ArrayList<>();
//         List<Recipe> semiMatches = new ArrayList<>();
    
//         recipeContainer.add(perfMatches);
//         recipeContainer.add(semiMatches);
        
//         for(Recipe recipe : recipeDatabase) {
//             int matchNum = matchesAllPreferences(recipe, preferences); //Cache the result
//             if(matchNum == PERFECT_MATCH) { perfMatches.add(recipe); }
//             else if(matchNum == SEMI_MATCH) { semiMatches.add(recipe); }
//         }
        
//         return recipeContainer;
//     }
    
//     // ❗Do not delete❗
//     private int matchesAllPreferences(Recipe recipe, Preferences prefs) {
//         int count = 0; //Counter for number of satisfied preferences
//         count += (matchesDietaryRestrictions(recipe, prefs)) ? 1 : 0;
//         count += (matchesHealthGoals(recipe, prefs)) ? 1 : 0;
//         count += (matchesCuisinePreferences(recipe, prefs)) ? 1 : 0;
        
//         //getCookTime() --> returns the cook time of the recipe.
//         //getMaxCookTime() --> returns the willingness to wait selected in the users preference
//         count += (prefs.getMaxCookTime() > 0 && recipe.getCookTime() > prefs.getMaxCookTime()) ? 1 : 0;

//         //getCost() --> returns the cost of the recipe
//         //getMaxBudget() --> returns the willingness to pay selected in the users preference
//         count += (prefs.getMaxBudget() > 0 && recipe.getCost() > prefs.getMaxBudget()) ? 1 : 0;

//         //countIngredientMatches returns 0 or 1
//         count += (countIngredientMatches(recipe, prefs.getAvailableIngredients()));

//         return count;
//     }

//     //Check if the current recipe has 90% of the user ingredients
//     private int countIngredientMatches(Recipe recipe, List<String> availableIngredients) {
//         int matches = 0;
//         List<String> ner = recipe.getNER();

//         //ioh --> Ingredient On Hand
//         for(String ioh : availableIngredients)
//         {
//             if(ner.contains(ioh)) { matches += 1; }
//         }
        
//         return matches >= (int)(availableIngredients.size() * 0.9) ? 1 : 0;
//     }
    
//     //Do we want perfect match? Do we want more than selected?
//     private boolean matchesDietaryRestrictions(Recipe recipe, Preferences prefs) {
//         String dietaryCategory = recipe.getDietaryCategory().toLowerCase();
        
//         if (prefs.isVegetarian() && !dietaryCategory.contains("vegetarian")) {
//             return false;
//         }
        
//         if (prefs.isKeto() && !dietaryCategory.contains("keto")) {
//             return false;
//         }
        
//         if (prefs.isGlutenFree() && !dietaryCategory.contains("gluten-free")) {
//             return false;
//         }
        
//         return true;
//     }
    
//     private boolean matchesHealthGoals(Recipe recipe, Preferences prefs) {
//         int calories = recipe.getCalories();
        
//         if (prefs.isLowCalorie() && calories > 400) {
//             return false;
//         }
        
//         if (prefs.isHighCalorie() && calories < 600) {
//             return false;
//         }
        
//         if (prefs.isHighProtein() && calories < 500) {
//             return false;
//         }
        
//         return true;
//     }
    
//     private boolean matchesCuisinePreferences(Recipe recipe, Preferences prefs) {
//         String cuisine = recipe.getCuisine().toLowerCase();
        
//         if (!prefs.isItalian() && !prefs.isMexican() && !prefs.isAsian() && 
//             !prefs.isAmerican() && !prefs.isMediterranean()) {
//             return true;
//         }
        
//         if (prefs.isItalian() && cuisine.contains("italian")) {
//             return true;
//         }
//         if (prefs.isMexican() && cuisine.contains("mexican")) {
//             return true;
//         }
//         if (prefs.isAsian() && (cuisine.contains("asian") || cuisine.contains("chinese"))) {
//             return true;
//         }
//         if (prefs.isAmerican() && cuisine.contains("american")) {
//             return true;
//         }
//         if (prefs.isMediterranean() && cuisine.contains("mediterranean")) {
//             return true;
//         }
        
//         return false;
//     }
    
//     public void setRecipeDatabase(List<Recipe> recipes) {
//         this.recipeDatabase = new ArrayList<>(recipes);
//     }
// }
/**
 * Recipe.java
 *
 * Represents a recipe with its title, cuisine, cost, cooking time,
 * ingredients, instructions, and nutrition facts.
 */

package cookiq.models;

public class Recipe {
    private String name;
    private String cuisine;
    private String dietaryCategory;
    private int cookTime;
    private double cost;
    private int calories;
    
    public Recipe(String name, String cuisine, String dietaryCategory, int cookTime, double cost, int calories) {
        this.name = name;
        this.cuisine = cuisine;
        this.dietaryCategory = dietaryCategory;
        this.cookTime = cookTime;
        this.cost = cost;
        this.calories = calories;
    }
    
    // Getters
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getDietaryCategory() { return dietaryCategory; }
    public int getCookTime() { return cookTime; }
    public double getCost() { return cost; }
    public int getCalories() { return calories; }
}
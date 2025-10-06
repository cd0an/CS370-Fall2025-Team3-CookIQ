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
    
    public Recipe(String name, String cuisine, String dietaryCategory, int cookTime) {
        this.name = name;
        this.cuisine = cuisine;
        this.dietaryCategory = dietaryCategory;
        this.cookTime = cookTime;
    }
    
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getDietaryCategory() { return dietaryCategory; }
    public int getCookTime() { return cookTime; }
}

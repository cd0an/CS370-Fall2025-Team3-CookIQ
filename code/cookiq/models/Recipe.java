/**
 * Recipe.java
 *
 * Represents a recipe with its title, cuisine, cost, cooking time,
 * ingredients, instructions, and nutrition facts.
 */

package cookiq.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private List<String> directions;
    private List<String> NER; //Raw ingredients
    private String dietaryCategory;
    private int calories;
    private String cuisine;
    private int cookTime;
    private double cost;
    
    public Recipe() {};

    public Recipe(String id, String name, String cuisine, String dietaryCategory, 
    int cookTime, double cost, int calories, List<String> ingredients,
    List<String> directions, List<String> NER) {
        this.ingredients = ingredients;
        this.directions = directions;
        this.NER = NER;
        this.name = name;
        this.cuisine = cuisine;
        this.dietaryCategory = dietaryCategory;
        this.cookTime = cookTime;
        this.cost = cost;
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                ", NER=" + NER +
                ", dietaryCategory='" + dietaryCategory + '\'' +
                ", healthGoals='" + calories + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", maxCookTime='" + cookTime + '\'' +
                ", budgetPerMeal='" + cost + '\'' +
                ", calories=" + calories +
                ", cookTime=" + cookTime +
                ", cost=" + cost +
                '}';
    }
    
    // Getters
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getDietaryCategory() { return dietaryCategory; }
    public int getCookTime() { return cookTime; }
    public double getCost() { return cost; }
    public int getCalories() { return calories; }
    public List<String> getIngredients() {return ingredients;}
    public List<String> getDirections() {return directions;}
    public List<String> getNER() {return NER;}

    public static Recipe parseRecipe(Document doc) {
        // Extract and sanitize fields
        String id = doc.getObjectId("_id").toHexString();
        String name = doc.getString("title");
        String cuisine = doc.getString("preferred_cuisines");
        String dietaryCategory = doc.getString("dietary_restrictions");
        List<String> ingredients = (List<String>) doc.get("ingredients");
        List<String> directions = (List<String>) doc.get("directions");
        List<String> NER = (List<String>) doc.get("NER");

        // Parse cook time (e.g., "> 30 min" → 30)
        int cookTime = 0;
        String timeString = doc.getString("max_cook_time");
        if (timeString != null) {
            timeString = timeString.replaceAll("[^0-9]", ""); // keep only digits
            if (!timeString.isEmpty()) {
                cookTime = Integer.parseInt(timeString);
            }
        }

        // Parse cost (e.g., "$10 - $20" → average 15.0)
        double cost = 0.0;
        String costString = doc.getString("budget_per_meal");
        if (costString != null) {
            String[] nums = costString.replaceAll("[$]", "").split("-");
            if (nums.length == 2) {
                cost = (Double.parseDouble(nums[0].trim()) + Double.parseDouble(nums[1].trim())) / 2.0;
            } else {
                cost = Double.parseDouble(nums[0].trim());
            }
        }

        // Calories might not be stored yet, default to 0
        int calories = 0;

        // Create and return Recipe object
        return new Recipe(id, name, cuisine, dietaryCategory, cookTime, cost, calories, ingredients, directions, NER);
    }



    // Helper to parse array strings like "[a, b, c]"
    private static List<String> parseArray(String arrayString) {
        arrayString = arrayString.replaceAll("^\\[|]$", ""); // remove brackets
        String[] items = arrayString.split(", (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // split on comma outside quotes
        List<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item.trim());
        }
        return list;
    }

}
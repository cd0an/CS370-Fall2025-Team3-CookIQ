/**
 * Recipe.java
 *
 * Represents a recipe with its title, cuisine, cost, cooking time,
 * ingredients, instructions, and nutrition facts.
 */

package cookiq.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

public class Recipe {
    private String id;
    private String name;
    private List<String> ingredients;
    private List<String> directions;
    private List<String> NER; //Raw ingredients
    private String dietaryCategory;
    private String healthGoals; // ONLY using healthGoals - no calories
    private String cuisine;
    private int cookTime;
    private double cost;
    
    public Recipe() {};

    // REMOVED calories completely - only using healthGoals
    public Recipe(String id, String name, String cuisine, String dietaryCategory, 
    int cookTime, double cost, String healthGoals, List<String> ingredients,
    List<String> directions, List<String> NER) {
        this.id = id;
        this.ingredients = ingredients;
        this.directions = directions;
        this.NER = NER;
        this.name = name;
        this.cuisine = cuisine;
        this.dietaryCategory = dietaryCategory;
        this.cookTime = cookTime;
        this.cost = cost;
        this.healthGoals = healthGoals;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                "title='" + name + '\'' +
                ", ingredients=" + ingredients +
                ", directions=" + directions +
                ", NER=" + NER +
                ", dietaryCategory='" + dietaryCategory + '\'' +
                ", healthGoals='" + healthGoals + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", cookTime=" + cookTime +
                ", cost=" + cost +
                '}';
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCuisine() { return cuisine; }
    public String getDietaryCategory() { return dietaryCategory; }
    public String getHealthGoals() { return healthGoals; } // This is what RecommendationService uses
    public int getCookTime() { return cookTime; }
    public double getCost() { return cost; }
    public List<String> getIngredients() {return ingredients;}
    public List<String> getDirections() {return directions;}
    public List<String> getNER() {return NER;}

    // NO getCalories() method - we don't have calories data

    public static Recipe parseRecipe(Document doc) {
        // Extract and sanitize fields
        String id = doc.getObjectId("_id").toHexString();
        String name = doc.getString("title");
        String cuisine = doc.getString("preferred_cuisines");
        String dietaryCategory = doc.getString("dietary_restrictions");
        String healthGoals = doc.getString("health_goals"); // This exists in your MongoDB
        List<String> ingredients = (List<String>) doc.get("ingredients");
        List<String> directions = (List<String>) doc.get("directions");
        List<String> NER = (List<String>) doc.get("NER");

        // Parse cook time (e.g., "> 30 min" → 30)
        int cookTime = 0;
        String timeString = doc.getString("max_cook_time");
        if (timeString != null) {
            timeString = timeString.replaceAll("[^0-9]", "");
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
            } else if (!nums[0].trim().isEmpty()) {
                cost = Double.parseDouble(nums[0].trim());
            }
        }

        // Create and return Recipe object - NO calories, only healthGoals
        return new Recipe(id, name, cuisine, dietaryCategory, cookTime, cost, healthGoals, ingredients, directions, NER);
    }

    // Helper to parse array strings like "[a, b, c]"
    private static List<String> parseArray(String arrayString) {
        arrayString = arrayString.replaceAll("^\\[|]$", "");
        String[] items = arrayString.split(", (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        List<String> list = new ArrayList<>();
        for (String item : items) {
            list.add(item.trim());
        }
        return list;
    }
}
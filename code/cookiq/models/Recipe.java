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

    public static Recipe parseDocument(Document doc) {
        Recipe recipe = new Recipe();

        // Strings
        recipe.name = doc.getString("title");
        recipe.ingredients = doc.getList("ingredients", String.class);
        recipe.directions = doc.getList("directions", String.class);
        recipe.NER = doc.getList("NER", String.class);
        recipe.dietaryCategory = doc.getString("dietary_restrictions");
        recipe.cuisine = doc.getString("preferred_cuisines");

        // Numeric fields
        recipe.calories = parseIntField(doc, "calories");
        recipe.cookTime = parseIntField(doc, "cookTime");
        recipe.cost = parseDoubleField(doc, "cost");

        return recipe;
    }

    // Helper: safely parse integer fields
    private static int parseIntField(Document doc, String fieldName) {
        if (!doc.containsKey(fieldName)) return 0;

        Object value = doc.get(fieldName);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            String digits = ((String) value).replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) {
                try {
                    return Integer.parseInt(digits);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }

    // Helper: safely parse double fields
    private static double parseDoubleField(Document doc, String fieldName) {
        if (!doc.containsKey(fieldName)) return 0.0;

        Object value = doc.get(fieldName);
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof String) {
            String digits = ((String) value).replaceAll("[^0-9.]", "");
            if (!digits.isEmpty()) {
                try {
                    return Double.parseDouble(digits);
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            }
        }
        return 0.0;
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
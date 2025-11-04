/**
 * Preferences.java
 *
 * Stores a user's selected dietary restrictions, health goals, cuisines,
 * cooking preferences, budget, and available ingredients.
 * Default constructor creates an Preference class with empty values and an ArrayList object. 
 * Other constructor loads in data for each variables/attributes.
 */

package cookiq.models;

import java.util.ArrayList; //ArrayList class for a resizeable array
import java.util.List; //Defines all behaviors that any list like DSA should have

public class Preferences {
    //Dietary restrictions
    private boolean vegetarian;
    private boolean keto;
    private boolean glutenFree;
    
    //Health goals
    private boolean lowCalorie;
    private boolean highCalorie;
    private boolean highProtein;
    
    //Cuisines
    private boolean italian;
    private boolean mexican;
    private boolean asian;
    private boolean american;
    private boolean mediterranean;
    
    //Cooking preferences
    private int maxCookTime; // in minutes
    private double maxBudget; // maximum cost per meal
    
    //Available ingredients
    private List<String> availableIngredients;
    
    public Preferences() { this.availableIngredients = new ArrayList<>(); }

    public Preferences(boolean vegetarian, boolean keto, boolean glutenFree, boolean lowCalorie, boolean highCalorie, boolean highProtein,
                        boolean italian, boolean mexican, boolean asian, boolean american, boolean mediterranean, int maxCookTime, 
                        double maxBudget, List<String> availableIngredients){
        this.vegetarian = vegetarian;
        this.keto = keto;
        this.glutenFree = glutenFree;
        this.lowCalorie = lowCalorie;
        this.highCalorie = highCalorie;
        this.highProtein = highProtein;
        this.italian = italian;
        this.mexican = mexican;
        this.asian = asian;
        this.american = american;
        this.mediterranean = mediterranean;
        this.maxCookTime = maxCookTime;
        this.maxBudget = maxBudget;
        this.availableIngredients = availableIngredients;

    }
    
    //Dietary restrictions setters/getters
    public boolean isVegetarian() { return vegetarian; }
    public void setVegetarian(boolean vegetarian) { this.vegetarian = vegetarian; }
    
    public boolean isKeto() { return keto; }
    public void setKeto(boolean keto) { this.keto = keto; }
    
    public boolean isGlutenFree() { return glutenFree; }
    public void setGlutenFree(boolean glutenFree) { this.glutenFree = glutenFree; }
    
    //Health goals setters/getters
    public boolean isLowCalorie() { return lowCalorie; }
    public void setLowCalorie(boolean lowCalorie) { this.lowCalorie = lowCalorie; }
    
    public boolean isHighCalorie() { return highCalorie; }
    public void setHighCalorie(boolean highCalorie) { this.highCalorie = highCalorie; }
    
    public boolean isHighProtein() { return highProtein; }
    public void setHighProtein(boolean highProtein) { this.highProtein = highProtein; }
    
    //Cuisine setters/getters
    public boolean isItalian() { return italian; }
    public void setItalian(boolean italian) { this.italian = italian; }
    
    public boolean isMexican() { return mexican; }
    public void setMexican(boolean mexican) { this.mexican = mexican; }
    
    public boolean isAsian() { return asian; }
    public void setAsian(boolean asian) { this.asian = asian; }
    
    public boolean isAmerican() { return american; }
    public void setAmerican(boolean american) { this.american = american; }
    
    public boolean isMediterranean() { return mediterranean; }
    public void setMediterranean(boolean mediterranean) { this.mediterranean = mediterranean; }
    
    //Practical preferences
    public int getMaxCookTime() { return maxCookTime; }
    public void setMaxCookTime(int maxCookTime) { this.maxCookTime = maxCookTime; }
    
    public double getMaxBudget() { return maxBudget; }
    public void setMaxBudget(double maxBudget) { this.maxBudget = maxBudget; }
    
    //Ingredients management
    public List<String> getAvailableIngredients() { return availableIngredients; }
    public void addAvailableIngredient(String ingredient) { 
        availableIngredients.add(ingredient.toLowerCase()); 
    }

    //Default all user preferences (set everything to default)
    public void clearAll() {
        //Dietary restrictions 
        vegetarian = keto = glutenFree = false;

        //Health goals
        lowCalorie = highCalorie = highProtein = false;

        //Cuisines 
        italian = mexican = asian = american = mediterranean = false;

        //Cooking preferences 
        maxCookTime = 0;
        maxBudget = 0.0;

        //Ingredients 
        availableIngredients.clear(); //Imported method from java.util.List
    }
}
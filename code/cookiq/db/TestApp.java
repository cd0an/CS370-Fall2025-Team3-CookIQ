package cookiq.db;

//Import the two below if it's outside the cookiq/db folder
import cookiq.db.RecipeRepository;
import cookiq.db.MongoDBConnection;
import cookiq.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TestApp {
    public static void main(String[] args) {        
        // //Test
        // List<Document> recipes = RecipeRepository.findByFilter("recipes", "NER=bacon", "dietary_restrictions=Keto");
        // System.out.println("Found " + recipes.size() + " recipes:");
        // System.out.println();
        // List<Recipe> parsedRecipes = new ArrayList<>();
        // for(Document recipe : recipes) {
        //     parsedRecipes.add(Recipe.parseRecipe(recipe));
        // }

        // for(Recipe recipe : parsedRecipes)
        // {
        //     System.out.println("[Name]\n" + recipe.getName());
        //     System.out.println("[Ingredients]\n" + recipe.getIngredients());
        //     System.out.println("[Directions]\n" + recipe.getDirections());
        //     System.out.println("[NER]\n" + recipe.getNER());
        //     System.out.println("[Dietary Category]\n" + recipe.getNER());
        //     System.out.println("[Health Goals]\n" + recipe.getCalories());
        //     System.out.println("[Cuisine]\n" + recipe.getCuisine());
        //     System.out.println("[Cook Time]\n" + recipe.getCookTime());
        //     System.out.println("[Budget]\n" + recipe.getCost());
        //     System.out.println();
        // }

        Document userPref = RecipeRepository.getUserPreferences("Zhuo Cheng", null);
        List<Document> matchedRecipes = RecipeRepository.getRecipesByPreferences(userPref);

        for (Document recipe : matchedRecipes) {
            System.out.println(recipe.toJson());
        }
    }
}

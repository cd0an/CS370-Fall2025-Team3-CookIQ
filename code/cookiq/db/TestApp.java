package cookiq.db;

//Import the two below if it's outside the cookiq/db folder
import cookiq.db.RecipeRepository;
import cookiq.db.MongoDBConnection;
import cookiq.models.Recipe;

import java.util.List;

import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TestApp {
    public static void main(String[] args) {        
        //Test
        List<Document> recipes = RecipeRepository.findByFilter("recipes", "NER=bacon", "dietary_restrictions=Keto");
        System.out.println("Found " + recipes.size() + " recipes:");
        System.out.println();
        for (Document recipe : recipes) {
            Recipe rcp = Recipe.parseDocument(recipe);
            rcp.toString();
            System.out.println();
        }
    }
}

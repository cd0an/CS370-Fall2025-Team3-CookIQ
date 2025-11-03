package cookiq.tests;

import cookiq.services.UserService;
import java.util.List;

public class TestUserService {
    public static void main(String[] args) {
        UserService service = new UserService();

        // Simulate a user liking a recipe
        String username = "miguel"; // use one that exists in your users.txt
        String recipeName = "Buffalo Chicken Bowl";

        // Add a like
        service.addLikedRecipe(username, recipeName);
        System.out.println("Added like for: " + recipeName);

        // Now reload and check if it was saved
        UserService reloadService = new UserService();
        List<String> liked = reloadService.getLikedRecipes(username);

        System.out.println(" Liked recipes for " + username + ": " + liked);
    }
}

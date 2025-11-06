/**
 * UserService handles all account operations:
 * - Register new users
 * - Login existing users
 * - Save/load users from a file
 */

package cookiq.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import cookiq.db.UserRepository;
import cookiq.models.Preferences;
import cookiq.security.PasswordUtils;
import cookiq.utils.PreferencesUtils;

public class UserService {
    private final UserRepository userRepository;

    public UserService() {
        userRepository = new UserRepository();
    }

    //Register User
    public boolean registerUser(String username, String password) {
        return userRepository.registerUser(username, password);
    }

    //User login
    public boolean loginUser(String username, String password) {
        Document user = userRepository.getUser(username); //Gets entire user info
        if (user == null) return false;

        String storedHash = user.getString("passwordHash");
        String enteredHash = PasswordUtils.sha256(password);
        return PasswordUtils.slowEquals(storedHash, enteredHash); //Slow equals is used to compare the passwords in the same amount of time regardless of password length
    }

    //Setter - Liked recipes
    public boolean addLikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        if (likedRecipes == null) likedRecipes = new ArrayList<>();

        if (!likedRecipes.contains(recipeName)) {
            likedRecipes.add(recipeName);
            user.put("likedRecipes", likedRecipes);
            userRepository.updateUser(username, user);
            return true;
        }
        return false;
    }

    //Getter - Liked recipes
    public List<String> getLikedRecipes(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new ArrayList<>();
        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        return likedRecipes != null ? likedRecipes : new ArrayList<>();
    }

    
    public boolean removeLikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        List<String> likedRecipes = user.getList("likedRecipes", String.class);
        if (likedRecipes == null) likedRecipes = new ArrayList<>();

        if (likedRecipes.remove(recipeName)) {
            user.put("likedRecipes", likedRecipes);
            userRepository.updateUser(username, user);
            return true;
        }
        return false;
    }

    // ==================== Disliked Recipes ====================
    public boolean addDislikedRecipe(String username, String recipeName) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        List<String> dislikedRecipes = user.getList("dislikedRecipes", String.class);
        if (dislikedRecipes == null) dislikedRecipes = new ArrayList<>();

        if (!dislikedRecipes.contains(recipeName)) {
            dislikedRecipes.add(recipeName);
            user.put("dislikedRecipes", dislikedRecipes);
            userRepository.updateUser(username, user);
            return true;
        }
        return false;
    }

    public List<String> getDislikedRecipes(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new ArrayList<>();
        List<String> dislikedRecipes = user.getList("dislikedRecipes", String.class);
        return dislikedRecipes != null ? dislikedRecipes : new ArrayList<>();
    }

    //User Preference save/get
    public boolean saveUserPreferences(String username, Preferences prefs) {
        Document user = userRepository.getUser(username);
        if (user == null) return false;

        String prefStr = PreferencesUtils.toJsonString(prefs);
        user.put("preferences", prefStr);

        userRepository.updateUser(username, user);
        return true;
    }

    public Preferences getUserPreferences(String username) {
        Document user = userRepository.getUser(username);
        if (user == null) return new Preferences();

        String prefStr = user.getString("preferences");
        return PreferencesUtils.fromJsonString(prefStr);
    }
}
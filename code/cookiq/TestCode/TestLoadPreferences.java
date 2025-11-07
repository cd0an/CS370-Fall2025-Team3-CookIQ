/**
 * Tests the loading of user prefences
 */

 package cookiq.TestCode;

 import java.util.Arrays;

import cookiq.models.Preferences;
import cookiq.services.UserService;

public class TestLoadPreferences
{
    public static void main(String[] args)
    {
        //Creates a UserService object for this instance
        UserService user_service = new UserService();
        String username = "Zhuo Cheng";

        //Save user preferences
        Preferences new_prefs = new Preferences(true, true, false, true, false, false, false, false, true, false, false, 15, 30, Arrays.asList("tomatos", "apples", "cucumbers"));
        user_service.saveUserPreferences(username, new_prefs); //Returned boolean is ignored

        //Load user preferences
        System.out.println();
        System.out.println("Loading in " + username + "'s preferences");
        Preferences loaded_prefs = user_service.getUserPreferences("Zhuo Cheng");
        user_service.printPreferences(loaded_prefs);
    }
}
package cookiq.services;

import org.bson.Document;
import com.google.gson.Gson;
import cookiq.models.Preferences;

public class PreferencesUtils {
    private static final Gson gson = new Gson();

    // Convert Preferences object to JSON string
    public static String toJsonString(Preferences prefs) {
        return prefs != null ? gson.toJson(prefs) : "{}";
    }

    // Convert JSON string to Preferences object
    public static Preferences fromJsonString(String json) {
        if (json == null || json.isEmpty()) return new Preferences();
        return gson.fromJson(json, Preferences.class);
    }

    // Convert Preferences to BSON Document for MongoDB
    public static Document toDocument(Preferences prefs) {
        return Document.parse(toJsonString(prefs));
    }

    // Convert BSON Document to Preferences
    public static Preferences fromDocument(Document doc) {
        if (doc == null) return new Preferences();
        return gson.fromJson(doc.toJson(), Preferences.class);
    }
}

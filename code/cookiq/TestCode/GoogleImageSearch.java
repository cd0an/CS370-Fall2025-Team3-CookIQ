package cookiq.TestCode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleImageSearch {
    // Replace with your credentials
    private static final String API_KEY = "AIzaSyCHa3fu4NsDuWU8WejIpNnFfHQm9_11p2w"; //API Key for image search
    private static final String CX = "83632e2b5a7c14e72"; //Search Engine ID

    public static void main(String[] args) {
        try {
            String query = "Caprese Stuffed Avocados";
            String encodedQuery = URLEncoder.encode(query, "UTF-8");

            String urlStr = String.format(
                "https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s&searchType=image&num=5",
                API_KEY, CX, encodedQuery
            );

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            // Parse JSON
            JSONObject json = new JSONObject(response.toString());
            JSONArray items = json.getJSONArray("items");

            System.out.println("Top Image Results:\n");
            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                System.out.println((i + 1) + ". " + item.getString("link"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

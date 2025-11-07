package cookiq.services;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject; 

public class ImageService {
    /**
     * API_KEY --> Api key
     * CX --> Search Engine ID
     */
    private static final String API_KEY = "AIzaSyCHa3fu4NsDuWU8WejIpNnFfHQm9_11p2w";
    private static final String CX = "83632e2b5a7c14e72";

    /**
     * Method to get 5 (or more, but 5 for now) images based on passed in recipe name
     * Buffered --> Means editable image
     */
    public List<BufferedImage> getImage(String recipe_name)
    {
        List<BufferedImage> image_list = new ArrayList<>();
        try
        {
            String query = recipe_name;
            String encoded_query = URLEncoder.encode(query, "UTF-8"); //Allows the query to be URL search friendly

            //Finds returns 5 images urls for the queried recipe name
            String url_str = String.format(
                "https://www.googleapis.com/customsearch/v1?key=%s&cx=%s&q=%s&searchType=image&num=5",
                API_KEY, CX, encoded_query
            );

            URL url = new URL(url_str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET"); //The Https URL connection

            //Read response
            StringBuilder response = new StringBuilder();
            try(BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) 
            {
                String line;                
                while((line = in.readLine()) != null) 
                {
                    response.append(line); //if line isn't null we append it to create the final url
                }
            }

            JSONObject json = new JSONObject(response.toString());
            JSONArray items = json.getJSONArray("items"); //Returned json of 5 urls

            for (int i = 0; i < items.length(); i++) 
            {
                JSONObject item = items.getJSONObject(i);
                String image_url = item.getString("link"); //Get the url link from the JSONObject
                BufferedImage img = ImageIO.read(new URL(image_url));
                image_list.add(img);
            }
        }

        /**
         * Error catching
         */
        catch(MalformedURLException e) 
        {
            e.printStackTrace(); //Invalid URL format
        } 
        catch(IOException e) 
        {
            e.printStackTrace(); //Connection/stream errors
        } 
        catch(JSONException e) 
        {
            e.printStackTrace(); //JSON parsing errors
        }
        return image_list;
    }
}

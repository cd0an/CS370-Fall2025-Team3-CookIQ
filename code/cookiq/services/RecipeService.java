package cookiq.services;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

public class RecipeService {
    private static final String SUPABASE_URL = "https://veewnmnuitmbhxgborud.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZlZXdubW51aXRtYmh4Z2JvcnVkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTg5NTM2MDEsImV4cCI6MjA3NDUyOTYwMX0.CDt5iuzwDjI6oOaco5LPnt902_Pq8lmAIrpSaEo32DI";
    
    private static final HttpClient client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .build();
    
    public static void main(String[] args) {
        System.out.println("=== Supabase Java Client Test ===");
        
        try {
            // Step 1: Test the connection
            System.out.println("Step 1: Testing connection...");
            testConnection();
            
            System.out.println("\nStep 2: Reading from table...");
            readFromTable("Smaller_Kaggle_Recipe_CSV");
            
            // System.out.println("\nStep 3: Inserting data...");
            // insertUser("users", "John Doe", "john@example.com");
            
        } catch (Exception e) {
            System.err.println("❌ Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void testConnection() throws Exception {
        String url = SUPABASE_URL + "/rest/v1/";
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("apikey", SUPABASE_KEY)
            .header("Authorization", "Bearer " + SUPABASE_KEY)
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200 || response.statusCode() == 404) {
            System.out.println("✅ Connection successful! Supabase is responding.");
            System.out.println("   Status code: " + response.statusCode());
            System.out.println("   Your Java app can communicate with Supabase!");
        } else {
            throw new Exception("Connection failed with status: " + response.statusCode());
        }
    }
    
    public static void readFromTable(String tableName) throws Exception {
        System.out.println("Reading from table: " + tableName);
        
        String url = SUPABASE_URL + "/rest/v1/" + tableName + "?select=*&limit=5";
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("apikey", SUPABASE_KEY)
            .header("Authorization", "Bearer " + SUPABASE_KEY)
            .header("Content-Type", "application/json")
            .GET()
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Response status: " + response.statusCode());
        
        if (response.statusCode() == 200) {
            System.out.println("✅ Data retrieved successfully!");
            System.out.println("Raw data from " + tableName + ":");
            System.out.println(response.body());
        } else {
            System.out.println("❌ Failed to read from table: " + tableName);
            System.out.println("Response: " + response.body());
            
            if (response.statusCode() == 404) {
                System.out.println("💡 This might mean the table doesn't exist.");
            }
        }
    }
    
    public static void insertUser(String tableName, String name, String email) throws Exception {
        System.out.println("Inserting user: " + name);
        
        // Create JSON manually (simple approach)
        String userData = String.format(
            "{\"name\": \"%s\", \"email\": \"%s\"}", 
            name, email
        );
        
        String url = SUPABASE_URL + "/rest/v1/" + tableName;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("apikey", SUPABASE_KEY)
            .header("Authorization", "Bearer " + SUPABASE_KEY)
            .header("Content-Type", "application/json")
            .header("Prefer", "return=representation")
            .POST(HttpRequest.BodyPublishers.ofString(userData))
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            System.out.println("✅ User created successfully!");
            System.out.println("Response: " + response.body());
        } else {
            System.out.println("❌ Failed to create user");
            System.out.println("Status: " + response.statusCode());
            System.out.println("Response: " + response.body());
        }
    }
    
    public static void updateUser(String tableName, int userId, String newName) throws Exception {
        System.out.println("Updating user with ID: " + userId);
        
        String updateData = String.format("{\"name\": \"%s\"}", newName);
        String url = SUPABASE_URL + "/rest/v1/" + tableName + "?id=eq." + userId;
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("apikey", SUPABASE_KEY)
            .header("Authorization", "Bearer " + SUPABASE_KEY)
            .header("Content-Type", "application/json")
            .header("Prefer", "return=representation")
            .method("PATCH", HttpRequest.BodyPublishers.ofString(updateData))
            .build();
        
        HttpResponse<String> response = client.send(request, 
            HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            System.out.println("✅ User updated successfully!");
            System.out.println("Response: " + response.body());
        } else {
            System.out.println("❌ Failed to update user");
            System.out.println("Response: " + response.body());
        }
    }
}
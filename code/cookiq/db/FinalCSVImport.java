package cookiq.db;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class FinalCSVImport {
    public static void main(String[] args) {
        String csvFile = "C:\\Users\\zczha\\Downloads\\recipe_csv_ner.csv";
        
        try {
            MongoDatabase database = MongoDBConnection.getDatabase();
            MongoCollection<Document> collection = database.getCollection("Recipes");
            
            collection.drop();
            collection = database.getCollection("Recipes");
            
            System.out.println("Reading CSV from: " + csvFile);
            
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            String headerLine = br.readLine();
            
            // Parse headers properly (no quotes in header line, so simple split works)
            String[] headers = headerLine.split(",");
            System.out.println("Found " + headers.length + " columns:");
            for (String h : headers) {
                System.out.println("  - " + h);
            }
            
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                String[] values = parseCSVLine(line);
                
                if (values.length < headers.length) {
                    System.out.println("Skipping malformed line " + (count + 1));
                    continue;
                }
                
                Document doc = new Document();
                for (int i = 0; i < headers.length; i++) {
                    String header = headers[i].trim();
                    String value = values[i].trim();
                    
                    // Parse integers
                    if (header.equals("cookTime") || header.equals("calories")) {
                        try {
                            doc.append(header, Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            doc.append(header, value);
                        }
                    } else {
                        doc.append(header, value);
                    }
                }
                
                collection.insertOne(doc);
                count++;
                
                if (count % 20 == 0) {
                    System.out.println("Imported: " + count);
                }
            }
            
            br.close();
            
            System.out.println("\n✓ Total imported: " + count);
            
            // Verify the structure
            System.out.println("\n=== Verification ===");
            Document sample = collection.find().first();
            if (sample != null) {
                System.out.println("Fields in document:");
                sample.keySet().forEach(key -> {
                    if (!key.equals("_id")) {
                        System.out.println("  " + key + ": " + sample.get(key));
                    }
                });
            }
            
            MongoDBConnection.close();
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }
}
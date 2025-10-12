/**
 * RecipeRepository.java
 *
 * Reads the loaded recipes from the MongoDB dataset and converts them into Recipe objects.
 */

package cookiq.db;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;

public class RecipeRepository
{
    private static final MongoDatabase db = MongoDBConnection.getDatabase();

    //Insertion
    //1st param --> table name
    //2nd param --> a row to be stored
    public static void insertDocument(String collectionName, Document doc)
    {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(doc);
        System.out.println("Inserted document into " + collectionName + ": " + doc.toJson());
    }

    //Find documents in a collection
    public static FindIterable<Document> findAll(String collectionName)
    {
        MongoCollection<Document> collection = db.getCollection((collectionName));
        return collection.find();
    }

    //Find documents given a filter
    public static FindIterable<Document> findGivenFilter(String collectionName, Document filter)
    {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        return collection.find(filter);
    }

    //Delete documents
    public static void delete(String collectionName, Document filter)
    {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.deleteMany(filter);
        System.out.println("Deleted documents given filter: " + filter.toJson());
    }

    //Modify documents
    public static void modify(String collectionName, Document filter, Document modification)
    {
        MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.updateMany(filter, new Document("$set", modification));
        System.out.println("Updated documents matching: " + filter.toJson());
    }
}
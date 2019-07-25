package com.qin.demo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoUtil {

    //用户名、密码、IP、端口、默认库名
    private static final String url = String.format("mongodb://%s:%s@%s:%d/%s", "mongo", "373616885", "47.100.185.77", 27017, "mongo");

    /**
     * / MongoClient 实例代表一个操作库的连接池--即使是多线程
     */
    private static final MongoClient mongoClient = new MongoClient(new MongoClientURI(url));

    /**
     * Create a Document
     */
    public static void createDocument() {

        MongoDatabase database = mongoClient.getDatabase("mongo");
        MongoCollection<Document> collection = database.getCollection("use");
        /**
         * JSON document:
         *  {
         *    "name" : "MongoDB",
         *    "type" : "database",
         *    "count" : 1,
         *    "versions": [ "v3.2", "v3.0", "v2.6" ],
         *    "info" : { x : 203, y : 102 }
         *   }
         */
        // The BSON 的数组类型对应 Java的 java.util.List
        // If no top-level _id field is specified in the document, MongoDB automatically adds the _id field to the inserted document.
        Document doc = new Document("name", "MongoDB")
                .append("age", 30)
                .append("password", "373616885")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));

        // Insert a Document
        collection.insertOne(doc);


        /**
         * { "i" : value }
         */
        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < 100; i++) {
            documents.add(new Document("i", i));
        }
        // Insert Multiple Documents
        collection.insertMany(documents);

    }

    public static void countDocumentsCollection() {
        MongoDatabase database = mongoClient.getDatabase("mongo");
        MongoCollection<Document> collection = database.getCollection("use");
        System.out.println(collection.countDocuments());
    }


    public static void main(String[] args) {
        createDocument();
        countDocumentsCollection();

    }

}

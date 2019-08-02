package com.qin.mongo;

import com.mongodb.Block;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author qinjp
 * @date 2019-07-24
 **/
public class MongoUtil {

    private static final String url = String.format("mongodb://%s:%s@%s:%d/%s", "mongo", "373616885", "47.100.185.77", 27017, "mongo");

    private static final MongoClient mongoClient = MongoClients.create(url);

    /**
     * Access a Database
     * If a database does not exist, MongoDB creates the database
     */
    public static MongoDatabase accessDatabase(final String databaseName) {
        return mongoClient.getDatabase(databaseName);
    }

    /**
     * Access a Collection
     * If a collection does not exist, MongoDB creates the collection
     */
    public static MongoCollection<Document> accessCollection(final String collectionName) {
        MongoDatabase database = mongoClient.getDatabase("mongo");
        return database.getCollection(collectionName);
    }

    /**
     * Create Document
     */
    public static void createDocument() {
        MongoCollection<Document> use = MongoUtil.accessCollection("info");
        /*
         *  {
         *    "name" : "MongoDB",
         *    "type" : "database",
         *    "count" : 1,
         *    "versions": [ "v3.2", "v3.0", "v2.6" ],
         *    "info" : { x : 203, y : 102 }
         *   }
         */
        Document doc = new Document("name", "MongoDB")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 203).append("y", 102));
        // Insert a Document
        use.insertOne(doc);

        // Insert Multiple Documents
        MongoCollection<Document> list = MongoUtil.accessCollection("list");
        /*
         * { "i" : value }
         */
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            documents.add(new Document("i", i));
        }

        list.insertMany(documents);

    }

    /**
     * Count Documents in A Collection
     */
    public static long countDocuments() {
        MongoCollection<Document> list = MongoUtil.accessCollection("list");
        return list.countDocuments();
    }


    /**
     * Query the Collection
     */
    public static void query() {
        MongoCollection<Document> collection = MongoUtil.accessCollection("list");
        // 查询第一个
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println("cursor : " + cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }


        for (Document cur : collection.find()) {
            System.out.println("for : " + cur.toJson());
        }


        myDoc = collection.find(Filters.eq("i", 71)).first();
        System.out.println(myDoc.toJson());

        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document.toJson());
            }
        };

        Consumer<Document> printConsumer = new Consumer<Document>() {
            @Override
            public void accept(Document document) {
                System.out.println(document.toJson());
            }
        };
        // "i" > 50
        collection.find(Filters.gt("i", 50)).forEach(printConsumer);

        // 50 < i <= 100
        collection.find(Filters.and(Filters.gt("i", 50), Filters.lte("i", 70))).forEach(printBlock);

    }

    public void update () {
        MongoCollection<Document> collection = MongoUtil.accessCollection("list");
        UpdateResult updateResult = collection.updateMany(Filters.lt("i", 11), Filters.in("i", 100));
        System.out.println(updateResult.getModifiedCount());
    }

    public static void main(String[] args) {
        query();

        MongoCollection<Document> use = MongoUtil.accessCollection("info");
        /*
         *  {
         *    "name" : "MongoDB",
         *    "type" : "database",
         *    "count" : 1,
         *    "versions": [ "v3.2", "v3.0", "v2.6" ],
         *    "info" : { x : 203, y : 102 }
         *   }
         */
        Document doc = new Document("name", "BD2")
                .append("type", "database")
                .append("count", 1)
                .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                .append("info", new Document("x", 201).append("y", 101));
        // Insert a Document
        use.insertOne(doc);
        mongoClient.close();
    }

}

package com.qin.mongo;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author qinjp
 * @date 2019-08-02
 **/
public class MongoPojoUtil {

    private static final String url = String.format("mongodb://%s:%s@%s:%d/%s", "mongo", "373616885", "47.100.185.77", 27017, "mongo");

    public static void main(String[] args) {

        //设置编码解码器
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClient mongoClient = MongoClients.create(url);

        MongoDatabase database = mongoClient.getDatabase("mongo");

        MongoCollection<Person> collection = database.getCollection("people", Person.class);

        //Using the CodecRegistry
        collection = collection.withCodecRegistry(pojoCodecRegistry);

        deleteDocuments(collection);

    }

    public static void queryCollection(MongoCollection<Person> collection) {

        for (Person person : collection.find()) {
            System.out.println(person);
        }

        FindIterable<Person> peoples = collection.find(Filters.eq("address.city", "London"));
        for (Person p : peoples) {
            System.out.println(p);
        }

        Block<Person> printBlock = System.out::println;

        // "age" > 30
        collection.find(Filters.gt("age", 30)).forEach(printBlock);
    }


    public static void insertOne(MongoCollection collection) {
        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));
        collection.insertOne(ada);
    }

    public static void insertMany(MongoCollection collection) {
        List<Person> people = Arrays.asList(
                new Person("Charles Babbage", 45, new Address("5 Devonshire Street", "London", "W11")),
                new Person("Alan Turing", 28, new Address("Bletchley Hall", "Bletchley Park", "MK12")),
                new Person("Timothy Berners-Lee", 61, new Address("Colehill", "Wimborne", null))
        );
        collection.insertMany(people);
    }

    public static void updateDocuments(MongoCollection<Person> collection) {
        // Update a Single Person

        UpdateResult oneResult = collection.updateOne(Filters.eq("name", "Ada Lovelace"), Updates.combine(Updates.set("age", 20), Updates.set("name", "Ada Byron")));
        System.out.println(oneResult);

        // Update Multiple Persons
        UpdateResult updateResult = collection.updateMany(Filters.not(Filters.eq("address.zip", null)), Updates.set("address.zip", null));
        System.out.println(updateResult);

        // 删除字段
        UpdateResult unset = collection.updateMany(Filters.not(Filters.eq("address.zip", null)), Updates.unset("zip"));
        System.out.println(unset);


        Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));
        UpdateResult one = collection.replaceOne(Filters.eq("name", "Timothy Berners-Lee"), ada);
        System.out.println(one);
    }

    public static void deleteDocuments(MongoCollection<Person> collection) {

        DeleteResult one = collection.deleteOne(Filters.eq("address.city", "Bletchley Park"));
        System.out.println(one);

        DeleteResult deleteResult = collection.deleteMany(Filters.eq("address.zip", null));
        System.out.println(deleteResult);

    }

}



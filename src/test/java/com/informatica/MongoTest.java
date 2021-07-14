package com.informatica;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class MongoTest {
    @Test
    public void testMongo() {

        String port = System.getProperty("port");

        final ServerAddress serverAddress = new ServerAddress("localhost", Integer.parseInt(port));

        try(MongoClient mongoClient = new MongoClient(Collections.singletonList(serverAddress), MongoClientOptions.builder().build())) {
            MongoDatabase database = mongoClient.getDatabase("mydb");
            database.drop();
            database.createCollection("test");
            System.out.println(database.listCollectionNames());
            MongoCollection<Document> collection = database.getCollection("test");
            Document doc = new Document("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("versions", Arrays.asList("v3.2", "v3.0", "v2.6"))
                    .append("info", new Document("x", 203).append("y", 102));

            try(ClientSession clientSession = mongoClient.startSession()) {
                clientSession.startTransaction();
                collection.insertOne(clientSession, doc);
                clientSession.commitTransaction();
            }

            FindIterable<Document> documents = collection.find();
            for (Document document : documents) {
                System.out.println(document.toString());
            }
        }
    }
}

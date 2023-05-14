package org.example.mongo;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Hello world!
 */

public class MongoTest {
    public static void main(String[] args) {
        // Replace the uri string with your MongoDB deployment's connection string
//        String uri = "mongodb://root:example@192.168.1.150:27017/?socketKeepAlive=true&heartbeatFrequency=1000&maxConnectionIdleTime=600000";
        String uri = "mongodb://wedata_test:Wedata@12345@100.109.8.130:6521/?socketKeepAlive=true&heartbeatFrequency=1000&maxConnectionIdleTime=600000";
//        AtomicInteger atomicInteger = new AtomicInteger(3230);
//        AtomicLong atomicLong = new AtomicLong(0);
//        for (int i = 0; i < 5; i++) {
//            MongoThread mongoThread = new MongoThread(uri, atomicInteger);
//            mongoThread.start();
//        }

//        MongoClient mongoClient = MongoClients.create(uri);
//        MongoDatabase database = mongoClient.getDatabase("bb-typing");
//        MongoCollection<Document> type_word_263 = database.getCollection("type_word_263");
//        FindIterable<Document> limit = type_word_263.find().sort(Document.parse("{codeRight:1}")).limit(10);
//        for (Document document : limit) {
//            System.out.println(document.toJson());
//        }


        MongoClient mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("mgdb1");
//        MongoCollection<Document> collection = database.getCollection("all_data_test");
//        List<Document> cache = new LinkedList<>();
//        for (int j = 1; j <= 10000 * 20000; j++) {
//            int randomInt = RandomUtil.randomInt(0, 10000 * 50000);
//            TypeStatusCount typeStatusCount = new TypeStatusCount(j, randomInt, randomInt, randomInt, 0, 0, randomInt);
//            Document document = Document.parse(JSONObject.toJSONString(typeStatusCount));
//            cache.add(document);
//            if (cache.size() >= 1024) {
//                System.out.println("下标：" + j);
//                collection.insertMany(cache);
//                cache.clear();
//            }
//        }
    }

    public static class MongoThread extends Thread {

        private final AtomicInteger atomicInteger;

        private final MongoClient mongoClient;

        private final MongoDatabase database;

        public MongoThread(String uri, AtomicInteger atomicInteger) {
            this.atomicInteger = atomicInteger;
            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase("bb-typing");
        }

        @Override
        public void run() {
            int i = atomicInteger.getAndIncrement();
            while (i <= 10000) {
                List<String> list = new ArrayList<>();
                MongoIterable<String> listCollectionNames = database.listCollectionNames();
                for (String listCollectionName : listCollectionNames) {
                    list.add(listCollectionName);
                }
                String collectionName = "type_word_" + i;
                if (!list.contains(collectionName)) {
                    database.createCollection(collectionName);
                }
                MongoCollection<Document> collection = database.getCollection(collectionName);

                System.out.println("插入" + collectionName);
                List<Document> cache = new LinkedList<>();
                for (int j = 1; j <= 50000; j++) {
                    TypeStatusCount typeStatusCount = new TypeStatusCount(i, j, j, j, 0, 0, j);
                    Document document = Document.parse(JSONObject.toJSONString(typeStatusCount));
                    cache.add(document);
                    if (cache.size() >= 1024) {
                        System.out.println("插入" + collectionName + "下标：" + j);
                        collection.insertMany(cache);
                        cache.clear();
                    }
                }
                if (!cache.isEmpty()) {
                    collection.insertMany(cache);
                    cache.clear();
                }
                i = atomicInteger.getAndIncrement();
            }
            mongoClient.close();
        }
    }
}

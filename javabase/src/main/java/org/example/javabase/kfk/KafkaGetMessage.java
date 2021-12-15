package org.example.javabase.kfk;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author tfq
 * @date 2020/4/29 10:10
 */
public class KafkaGetMessage {
    public static void main(String[] args) {
        getMessage("kafka_test_3");
    }

    public static void getMessage(String topic) {
        String brokerList = "10.194.188.94:9092,10.194.188.95:9092,10.194.188.96:9092";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerList);
        properties.put("group.id", "test132111");// 消费者组id
        properties.put("enable.auto.commit", "true");//提交
        properties.put("auto.commit.interval.ms", "1000");//自动确认offset时间间隔
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//key 序列化类
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//value序列化类
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, value = %s", record.offset(), record.value());
                System.out.println("=====================>");
            }

        }

    }
}

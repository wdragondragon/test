package org.example.javabase.kfk;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author JDragon
 * @Date 2021.12.03 下午 3:46
 * @Email 1061917196@qq.com
 * @Des:
 */
public class KafkaOffset {
    public static void main(String[] args) {
        String topic = "kafka_test_4";
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
        Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
        List<PartitionInfo> partitionInfos = kafkaConsumer.partitionsFor(topic);
        for (PartitionInfo partitionInfo : partitionInfos) {
            int partition = partitionInfo.partition();
            TopicPartition topicPartition = new TopicPartition(topic, partition);
            kafkaConsumer.assign(Collections.singletonList(topicPartition));
            long position = kafkaConsumer.position(topicPartition);
            System.out.println(partition + ":" + position);
        }

        for (PartitionInfo partitionInfo : partitionInfos) {
            TopicPartition topicPartition = new TopicPartition(topic,partitionInfo.partition());
            OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(0L);
            offsets.put(topicPartition,offsetAndMetadata);
        }

        kafkaConsumer.commitSync(offsets);

    }
}

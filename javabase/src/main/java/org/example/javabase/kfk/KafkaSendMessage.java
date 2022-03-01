package org.example.javabase.kfk;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author tfq
 * @date 2020/4/28 21:08
 */
public class KafkaSendMessage {
    public static void main(String[] args) {
//        getTopics();
        sendMessage();

    }

    public static void getTopics() {
        String brokerList = "10.194.188.94:9092,10.194.188.95:9092,10.194.188.96:9092";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerList);
//        properties.put("group.id", "test");// 消费者组id
//        properties.put("enable.auto.commit", "true");//提交
//        properties.put("auto.commit.interval.ms", "1000");//自动确认offset时间间隔
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//key 序列化类
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//value序列化类
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

        Map<String, List<PartitionInfo>> listTopics = kafkaConsumer.listTopics();
        List<String> topics = new ArrayList<>();
        for (String map : listTopics.keySet()) {
            topics.add(map);
        }
        List<PartitionInfo> tfq_test = listTopics.get("ymt_ord_applet_order_detail");
        System.out.println("tfq_test:" + tfq_test);
        System.out.println("listTopics:" + topics);
    }

    public static void sendMessage() {
        String brokerList = "10.194.188.95:9092";
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerList);
//        properties.put("enable.auto.commit", "true");//提交
//        properties.put("auto.commit.interval.ms", "1000");//自动确认offset时间间隔
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        // 实例化出producer
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        try {
//            for (int i = 1; i < 10; i++) {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("id", i);
//                jsonObject.put("username", "username" + i);
                String json = "{'id': 10, 'dust_concentration': 466, 'dust_concentration_five_min': 459, 'monitor_time': '2022-01-27 10:24:29', 'monitor_site': '你好', 'monitor_code': 'hkl', 'monitor_lon': 727, 'monitor_lat': 276}";
                JSONObject jsonObject = JSONObject.parseObject(json);
                ProducerRecord<String, String> record = new ProducerRecord<>("ys_dust_sensor_topic_outside", jsonObject.toJSONString());
                // 发送消息
//                ProducerRecord<String, String> record = new ProducerRecord<>("kafka_test_4", i + "{sp}" + "username" + i);
                producer.send(record);
//                System.out.println("第" + i + "条:" + record.value());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

}

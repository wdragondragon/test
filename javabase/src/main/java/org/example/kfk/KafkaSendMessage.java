package org.example.kfk;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author tfq
 * @date 2020/4/28 21:08
 */
public class KafkaSendMessage {
   public static void main(String[] args) {
//      getTopics();
      sendMessage();

   }

   public static void getTopics() {
      String brokerList = "10.194.188.229:6667";
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
      List<PartitionInfo> tfq_test = listTopics.get("tfq_test");
      System.out.println("tfq_test:" + tfq_test);
      System.out.println("listTopics:" + topics);
   }

   public static void sendMessage() {
      String brokerList = "10.194.188.226:9092,10.194.188.231:6667";
      Properties properties = new Properties();
      properties.put("bootstrap.servers", brokerList);
      properties.put("group.id", "test");// 消费者组id
//        properties.put("enable.auto.commit", "true");//提交
//        properties.put("auto.commit.interval.ms", "1000");//自动确认offset时间间隔
      properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//key 序列化类
      properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//value序列化类
      properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      // 实例化出producer
      KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

      try {
         for (int i = 0; i < 10; i++) {
            JSONArray array = new JSONArray();
            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id",1);
//            jsonObject.put("custom_batch_replace","1");
//            jsonObject.put("add_default_vale","test");
//            jsonObject.put("null_value_filter","notNull");
//            jsonObject.put("aes_str","test");
//            jsonObject.put("split_index","test:order:list");
//            jsonObject.put("add_str","test");

            jsonObject.put("id","1");
            jsonObject.put("name","name"+i);

//            jsonObject.put("time", DateUtil.now());
            //String message = i + "," + "小龙女";
            //String message = "[{\"id\":"+i+",\"name\":\"zhangsan\",\"age\":23,\"sex\":\"男\",\"remark\":\"123456789\",\"create_time\":\"2020-03-02 11:21:23\"}]";
            //String message = "[{\"student_id\":"+i+",\"name\":\"李四\"}]";
            //String message = i + ",郭襄,女,136,3.24,描啊啊啊啊啊啊啊述";
//            String message = "{\"guid\":"+i+",\"bmtabletime\":\"2020-06-22\",\"OUTINVID\":\"edfg-gfds-09fgs-f3gr\",\"ANCHEID\":\"edfg-gfds-09fgs-f3gr\",\"ENTNAME\":\"华为\",\"UNISCID\":\"edfg-gfds-09fgs-f3gr\"}";
            //ProducerRecord<String, String> record = new ProducerRecord<String, String>("kafka_ces0001", "tfq", message);
            //ProducerRecord<String, String> record = new ProducerRecord<String, String>("kafka_ces", "tfq", message);
            //String message = "{\"id\":" + i + ",\"name\":\"黄大仙\"}";
            ProducerRecord<String, String> record = new ProducerRecord<>("kafka_test_tfq", "zhjl", jsonObject.toJSONString());
            // 发送消息
            producer.send(record);
            //System.out.println("第"+i+"条===");
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         producer.close();
      }
   }

}

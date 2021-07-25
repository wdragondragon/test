package org.example.kfk;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author tfq
 * @date 2020/4/29 10:10
 */
public class KafkaGetMessage {
   public static void main(String[] args) {
      getMessage("tfq_test");
   }

   public static void getMessage(String topic) {
//      try {
//         Properties properties = new Properties();
//         properties.put("zookeeper.connect", "10.194.188.229:2181");
//         properties.put("auto.commit.enable", "true");
//         properties.put("auto.commit.interval.ms", "60000");
//         properties.put("group.id", "test");
//         ConsumerConfig consumerConfig = new ConsumerConfig(properties);
//         ConsumerConnector javaConsumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
//
//         // topic的过滤器
//         Whitelist whitelist = new Whitelist(topic);
//         List<KafkaStream<byte[], byte[]>> partitions = javaConsumerConnector.createMessageStreamsByFilter(whitelist);
//
//         if (partitions == null) {
//            System.out.println("empty!");
//            TimeUnit.SECONDS.sleep(1);
//         }
//
//         System.out.println("partitions:" + partitions.size());
//         System.out.println("partitions大师傅似的：" + partitions);
//
//         // 消费消息
//         for (KafkaStream<byte[], byte[]> partition : partitions) {
//            System.out.println("partition:" + partition);
//            ConsumerIterator<byte[], byte[]> iterator = partition.iterator();
//            System.out.println(iterator.length());
//            while (iterator.hasNext()) {
//               MessageAndMetadata<byte[], byte[]> next = iterator.next();
//               System.out.println("bbbbb:"+next.toString());
//               System.out.println("partiton:" + next.partition());
//               System.out.println("offset:" + next.offset());
//               System.out.println("接收到message:" + new String(next.message(), "utf-8"));
//            }
//         }
//      } catch (InterruptedException e) {
//         e.printStackTrace();
//      } catch (UnsupportedEncodingException e) {
//         e.printStackTrace();
//      }
      String brokerList = "10.194.188.229:6667,10.194.188.231:6667";
      Properties properties = new Properties();
      properties.put("bootstrap.servers", brokerList);
      properties.put("group.id", "test");// 消费者组id
      properties.put("enable.auto.commit", "true");//提交
      properties.put("auto.commit.interval.ms", "1000");//自动确认offset时间间隔
      properties.put("auto.offset.reset", "latest");
      properties.put("session.timeout.ms", "30000");
      properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//key 序列化类
      properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//value序列化类
      properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);


      kafkaConsumer.subscribe(Arrays.asList(topic));
      while (true) {
         ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
         for (ConsumerRecord<String, String> record : records) {
            System.out.printf("offset = %d, value = %s", record.offset(), record.value());
            System.out.println("=====================>");
         }
      }
   }
}

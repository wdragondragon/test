package org.example.kafka.kerberos;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {

    private static final String KAFKA_BROKERS = "centos1:9092";
    private static final String KAFKA_USERNAME = "kafka/hadoop.hadoop.com@HADOOP.COM";
    private static final String KAFKA_CLIENT_KEYTAB_PATH = "D:/dev/IdeaProjects/test/database/kerberos/kafka-kerberos/src/main/resources/kafka@hadoop.keytab";
    private static final String KAFKA_TOPIC = "test";
    private static final String KAFKA_GROUP_ID = "your-kafka-consumer-group-id";
    private static final String SASL_JAAS_CONFIG_STRING_FORMAT = "com.sun.security.auth.module.Krb5LoginModule required "
            + "refreshKrb5Config=true "
            + "useKeyTab=true "
            + "keyTab=\"%s\" "
            + "principal=\"%s\" "
            + "useTicketCache=false "
            + "serviceName=\"kafka\";";
    // replace 'YOUR-REALM.COM' with your Kerberos realm name

    public static void main(String[] args) throws IOException {
        setKrb5Config("D:/dev/IdeaProjects/test/database/kerberos/kafka-kerberos/src/main/resources/krb5.conf");
        KafkaConsumer<String, String> consumer = KafkaConsumerExample.createKafkaConsumer();
        KafkaConsumerExample.printAllTopics(KafkaConsumerExample.createAdminClient());
        KafkaConsumerExample.consumeMessages(consumer);
    }

    public static KafkaConsumer<String, String> createKafkaConsumer() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");
        props.put("group.id", KAFKA_GROUP_ID);
        props.put("auto.offset.reset", "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // Set SASL_JAAS_CONFIG properties
        String jaasConfig = String.format(SASL_JAAS_CONFIG_STRING_FORMAT, KAFKA_CLIENT_KEYTAB_PATH, KAFKA_USERNAME);
        props.put("sasl.jaas.config", jaasConfig);
        return new KafkaConsumer<>(props);
    }

    public static void consumeMessages(KafkaConsumer<String, String> consumer) {
        consumer.subscribe(Collections.singletonList(KAFKA_TOPIC));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(1000);
            records.forEach(record -> {
                System.out.println(String.format("Received message: (%s, %s, %d, %d)",
                        record.key(), record.value(), record.partition(), record.offset()));
            });
        }
    }

    public static AdminClient createAdminClient() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");
        // Set SASL_JAAS_CONFIG properties
        String jaasConfig = String.format(SASL_JAAS_CONFIG_STRING_FORMAT, KAFKA_CLIENT_KEYTAB_PATH, KAFKA_USERNAME);
        props.put("sasl.jaas.config", jaasConfig);
        return AdminClient.create(props);
    }

    public static void printAllTopics(AdminClient adminClient) {
        ListTopicsResult topics = adminClient.listTopics();
        try {
            for (TopicListing topic : topics.listings().get()) {
                System.out.println(topic.name());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static final String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";
    /**
     * 设置krb5文件
     *
     * @param krb5ConfFile
     * @throws IOException
     */
    public static void setKrb5Config(String krb5ConfFile)
            throws IOException {
        System.setProperty(JAVA_SECURITY_KRB5_CONF, krb5ConfFile);
        String ret = System.getProperty(JAVA_SECURITY_KRB5_CONF);
        if (ret == null) {
            throw new IOException(JAVA_SECURITY_KRB5_CONF + " is null.");
        }
        if (!ret.equals(krb5ConfFile)) {
            throw new IOException(JAVA_SECURITY_KRB5_CONF + " is " + ret + " is not " + krb5ConfFile + ".");
        }
    }
}

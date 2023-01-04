package org.example.kafka.kerberos;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @Author JDragon
 * @Date 2022/11/10 14:29
 * @description:
 */
public class KafkaKrb5Test {
    public static void main(String[] args) throws IOException {
        Properties properties = login("", "", "");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        Map<String, List<PartitionInfo>> stringListMap = kafkaConsumer.listTopics();
        kafkaConsumer.close();
    }


    public static Properties login(String principal, String krb5ConfigPath, String keytabPath) throws IOException {
        setKrb5Config(krb5ConfigPath);
        setZookeeperServerPrincipal("zookeeper/hadoop.hadoop.com");
        Properties props = initProperties();
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("sasl.kerberos.service.name", "kafka");
        props.put("sasl.mechanism", "GSSAPI");
        props.put("kerberos.domain.name", "hadoop.hadoop.com");
        props.put("sasl.jaas.config", getModuleContext(principal, keytabPath));
        return props;
    }

    public static Properties initProperties() {
        Properties props = new Properties();
        // Broker连接地址
        props.put("bootstrap.servers", "");
        // Group id
        props.put("group.id", "DemoConsumer");// 消费者组id
        // 是否自动提交offset
        props.put("enable.auto.commit", "false");//提交
        // 自动提交offset的时间间隔
        props.put("auto.commit.interval.ms", "1000");
        // 会话超时时间
        props.put("session.timeout.ms", "30000");
        // 消息Key值使用的反序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//key 序列化类
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");//value序列化类  // 安全协议类型

//        props.put("security.protocol", "SASL_PLAINTEXT");
        // 服务名
//        props.put("sasl.kerberos.service.name", "kafka");
        // 域名


        return props;
    }

    /**
     * no JavaDoc
     */
    public enum Module {
        STORM("StormClient"), KAFKA("KafkaClient"), ZOOKEEPER("Client");

        private String name;

        private Module(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * is IBM jdk or not
     */
    private static final boolean IS_IBM_JDK = System.getProperty("java.vendor").contains("IBM");
    /**
     * line operator string
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * IBM jdk login module
     */
    private static final String IBM_LOGIN_MODULE = "com.ibm.security.auth.module.Krb5LoginModule required";

    /**
     * oracle jdk login module
     */
    private static final String SUN_LOGIN_MODULE = "com.sun.security.auth.module.Krb5LoginModule required";

    private static String getModuleContext(String userPrincipal, String keyTabPath) {
        StringBuilder builder = new StringBuilder();
        if (IS_IBM_JDK) {
//            builder.append(module.getName()).append(" {").append(LINE_SEPARATOR);
            builder.append(IBM_LOGIN_MODULE).append(LINE_SEPARATOR);
            builder.append("credsType=both").append(LINE_SEPARATOR);
            builder.append("principal=\"").append(userPrincipal).append("\"").append(LINE_SEPARATOR);
            builder.append("useKeytab=\"").append(keyTabPath).append("\"").append(LINE_SEPARATOR);
            //            builder.append("};").append(LINE_SEPARATOR);
        } else {
//            builder.append(module.getName()).append(" {").append(LINE_SEPARATOR);
            builder.append(SUN_LOGIN_MODULE).append(LINE_SEPARATOR);
            builder.append("useKeyTab=true").append(LINE_SEPARATOR);
            builder.append("keyTab=\"").append(keyTabPath).append("\"").append(LINE_SEPARATOR);
            builder.append("principal=\"").append(userPrincipal).append("\"").append(LINE_SEPARATOR);
            builder.append("useTicketCache=false").append(LINE_SEPARATOR);
            builder.append("storeKey=true").append(LINE_SEPARATOR);
            //            builder.append("};").append(LINE_SEPARATOR);
        }
        builder.append("debug=true;").append(LINE_SEPARATOR);

        return builder.toString();
    }


    /**
     * Zookeeper quorum principal.
     */
    public static final String ZOOKEEPER_AUTH_PRINCIPAL = "zookeeper.server.principal";

    /**
     * java security krb5 file path
     */
    public static final String JAVA_SECURITY_KRB5_CONF = "java.security.krb5.conf";

    public static void setZookeeperServerPrincipal(String zkServerPrincipal)
            throws IOException {
        System.setProperty(ZOOKEEPER_AUTH_PRINCIPAL, zkServerPrincipal);
        String ret = System.getProperty(ZOOKEEPER_AUTH_PRINCIPAL);
        if (ret == null) {
            throw new IOException(ZOOKEEPER_AUTH_PRINCIPAL + " is null.");
        }
        if (!ret.equals(zkServerPrincipal)) {
            throw new IOException(ZOOKEEPER_AUTH_PRINCIPAL + " is " + ret + " is not " + zkServerPrincipal + ".");
        }
    }


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
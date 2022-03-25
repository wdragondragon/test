package org.example.javabase;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @Author JDragon
 * @Date 2022.03.01 下午 3:48
 * @Email 1061917196@qq.com
 * @Des:
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class RabbitMQ {

    private String host;

    private int port;

    private String username;

    private String password;

    public Connection getConnection() throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();//MQ采用工厂模式来完成连接的创建
        //2.在工厂对象中设置连接信息(ip,port,virtualhost,username,password)
        factory.setHost(host);//设置MQ安装的服务器ip地址
        factory.setPort(port);//设置端口号
        //MQ通过用户来管理
        factory.setUsername(username);//设置用户名称
        factory.setPassword(password);//设置用户密码
        //3.通过工厂对象获取连接
        return factory.newConnection();
    }

    public void simplePublish(String queueName, Object o) {
        try (Connection connection = getConnection();
             Channel channel = connection.createChannel()) {
            String msg = JSON.toJSONString(o, SerializerFeature.WriteMapNullValue);
            AMQP.BasicProperties basicProperties = new AMQP.BasicProperties();
            basicProperties = basicProperties.builder().contentType("json").build();
            channel.basicPublish("", queueName, basicProperties, msg.getBytes());
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void main(String[] args) {
        RabbitMQ rabbitMQ = new RabbitMQ();
        rabbitMQ.setHost("39.96.83.89");
        rabbitMQ.setPort(5672);
        rabbitMQ.setUsername("guest");
        rabbitMQ.setPassword("guest");


        Map<String, String> map = new HashMap<>();


        map.put("jobId", String.valueOf(102));
        map.put("receivedCount", String.valueOf(1));
        map.put("acceptedCount", String.valueOf(1));
        map.put("failedCount", String.valueOf(1));
        map.put("startTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        map.put("stopTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        map.put("logName", "logName" + System.currentTimeMillis());
        rabbitMQ.simplePublish("flumeIndicate2", map);
    }
}

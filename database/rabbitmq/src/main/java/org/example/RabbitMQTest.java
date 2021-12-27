package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 */
public class RabbitMQTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        String QUEUE_NAME = "test_queue_1";
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        String msg = "hello";
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
        channel.close();
        connection.close();

    }


    public static Connection getConnection() throws IOException, TimeoutException {
        //1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();//MQ采用工厂模式来完成连接的创建
        //2.在工厂对象中设置连接信息(ip,port,virtualhost,username,password)
        factory.setHost("10.194.188.76");//设置MQ安装的服务器ip地址
        factory.setPort(5672);//设置端口号
        //MQ通过用户来管理
        factory.setUsername("guest");//设置用户名称
        factory.setPassword("guest");//设置用户密码
        //3.通过工厂对象获取连接
        return factory.newConnection();
    }


}

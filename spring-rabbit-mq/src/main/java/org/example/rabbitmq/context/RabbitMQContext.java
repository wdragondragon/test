package org.example.rabbitmq.context;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author JDragon
 * @Date 2023/1/4 17:59
 * @description:
 */
public class RabbitMQContext {
    public static List<String> sendList = new CopyOnWriteArrayList<>();
    public static List<String> getList = new CopyOnWriteArrayList<>();
}

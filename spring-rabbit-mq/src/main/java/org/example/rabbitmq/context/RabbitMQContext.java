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

    public static List<Integer> sendList = new CopyOnWriteArrayList<>();

    public static List<Integer> runList = new CopyOnWriteArrayList<>();
    public static List<Integer> finishList = new CopyOnWriteArrayList<>();


    public static List<Integer> getUnFinish() {
        LinkedList<Integer> runList = new LinkedList<>(RabbitMQContext.runList);
        LinkedList<Integer> finishList = new LinkedList<>(RabbitMQContext.finishList);
        runList.removeAll(finishList);
        return runList;
    }

    public static List<Integer> getUnReceived() {
        LinkedList<Integer> sendList = new LinkedList<>(RabbitMQContext.sendList);
        LinkedList<Integer> runList = new LinkedList<>(RabbitMQContext.runList);
        LinkedList<Integer> finishList = new LinkedList<>(RabbitMQContext.finishList);
        sendList.removeAll(finishList);
        sendList.removeAll(runList);
        return sendList;
    }

}

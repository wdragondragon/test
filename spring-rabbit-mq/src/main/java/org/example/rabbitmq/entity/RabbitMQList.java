package org.example.rabbitmq.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author JDragon
 * @Date 2023/1/5 10:10
 * @description:
 */
@Data
public class RabbitMQList {

    private List<Integer> unReceived;

    private List<Integer> unFinish;

}

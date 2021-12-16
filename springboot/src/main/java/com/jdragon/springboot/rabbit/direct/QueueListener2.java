package com.jdragon.springboot.rabbit.direct;

import com.jdragon.springboot.rabbit.ExchangeType;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author JDragon
 * @Date 2021.04.15 上午 12:33
 * @Email 1061917196@qq.com
 * @Des:
 */


@Component
@RabbitListener(queues = ExchangeType.DIRECT + "-queue")
public class QueueListener2 {
    @RabbitHandler
    public void proces(String message) {
        System.out.println("消费者2:" + message);
    }
}

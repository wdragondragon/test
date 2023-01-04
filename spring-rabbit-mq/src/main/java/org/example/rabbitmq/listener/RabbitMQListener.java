package org.example.rabbitmq.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmq.context.RabbitMQContext;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * @Author JDragon
 * @Date 2023/1/4 17:28
 * @description:
 */

@Slf4j
@EnableRabbit
@Component
public class RabbitMQListener {
    /**
     * 消费日志
     */
//    @RabbitListener(bindings = {
//            @QueueBinding(value = @Queue(name = "logDetails-fanout"),
//                    exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "logDetails-fanout"))
//    }, concurrency = "1-100")
    @RabbitListener(queuesToDeclare = @Queue("logDetails_test"), concurrency = "1-100")
    public void logDetails(String s) {
        if (log.isDebugEnabled()) {
            log.debug("消费rabbitMQ logDetails：[{}]", s);
        }
        RabbitMQContext.getList.add(s);
        LinkedList<String> getList = new LinkedList<>(RabbitMQContext.getList);
        LinkedList<String> sendList = new LinkedList<>(RabbitMQContext.sendList);
        sendList.removeAll(getList);
        log.info("剩余：{}", sendList.size());
    }
}

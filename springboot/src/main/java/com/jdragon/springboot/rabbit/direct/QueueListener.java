package com.jdragon.springboot.rabbit.direct;

import com.jdragon.springboot.rabbit.ExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @Author JDragon
 * @Date 2021.04.15 上午 12:33
 * @Email 1061917196@qq.com
 * @Des:
 */

@Slf4j
@Component
public class QueueListener {
    @RabbitListener(queues = ExchangeType.DIRECT + "-queue")
    public void proces(Channel channel, Message message) throws IOException {
        System.out.println("消费者:" + new String(message.getBody(), StandardCharsets.UTF_8));
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (IOException e) {
            if (message.getMessageProperties().getRedelivered()) {
                log.info("消息已重复处理失败,拒绝再次接收");
                // 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.info("消息即将再次返回队列处理");
                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            }
            //e.printStackTrace();
        }
    }
}

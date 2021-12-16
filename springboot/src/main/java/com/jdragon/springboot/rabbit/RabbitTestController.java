package com.jdragon.springboot.rabbit;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author JDragon
 * @Date 2021.04.15 上午 12:34
 * @Email 1061917196@qq.com
 * @Des:
 */
@RestController
@RequestMapping("/rabbit")
public class RabbitTestController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 实现消息的发送
     */
    @GetMapping(value = "/" + ExchangeType.DIRECT)
    public void send() {
        String message = "this is queue test!";
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(ExchangeType.DIRECT + "-queue",message);
//            this.amqpTemplate.convertAndSend(ExchangeType.DIRECT + "-queue", message);
        }
    }
}

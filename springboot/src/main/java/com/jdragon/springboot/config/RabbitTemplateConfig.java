package com.jdragon.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author JDragon
 * @Date 2021.07.16 下午 5:32
 * @Email 1061917196@qq.com
 * @Des:
 */

@Slf4j
@Component
public class RabbitTemplateConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            log.info("消息发送成功:,{}", correlationData);
        } else {
            log.info("消息发送失败:,{}", cause);
        }
    }

    /**
     * 回调
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息主体:,{}", message);
        log.info("应答码:,{}", replyCode);
        log.info("描述:,{}", replyText);
        log.info("消息使用的交换器 exchange :,{}", exchange);
        log.info("消息使用的路由键 routing :,{}", routingKey);
    }
}

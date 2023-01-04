package org.example.rabbitmq.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmq.context.RabbitMQContext;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author JDragon
 * @Date 2023/1/4 17:30
 * @description:
 */
@Slf4j
@Api(tags = "quartz测试")
@RestController
@RequestMapping("/test")
public class RabbitMQController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private AtomicBoolean tag = new AtomicBoolean(false);

    private final AtomicInteger num = new AtomicInteger(0);

    @GetMapping("/sendMsg")
    @ApiOperation("发送消息")
    public String sendMsg(@RequestParam String msg) {
        rabbitTemplate.convertAndSend("logDetails_test", msg);
        return "成功";
    }

    @GetMapping("/sendMsgTag")
    @ApiOperation("开启循环发送消息")
    public String sendMsgTag() {
        this.tag.set(!this.tag.get());
        this.num.set(0);
        return "成功";
    }

    @GetMapping("/getRabbitList")
    @ApiOperation("获取发送了，但未消费到消息")
    public List<String> getRabbitList() {
        return RabbitMQContext.sendList;
    }

    @PostConstruct
    public void sendMsgRing() {
        log.info("开始发送消息");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (tag.get()) {
                    String s = String.valueOf(num.incrementAndGet());
                    rabbitTemplate.convertAndSend("logDetails_test", s);
                    RabbitMQContext.sendList.add(s);
                }
            }
        });
        for (int i = 0; i < 5; i++) {
            executorService.submit(thread);
        }
    }

}

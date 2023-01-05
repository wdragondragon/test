package org.example.rabbitmq.controller;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmq.context.RabbitMQContext;
import org.example.rabbitmq.entity.JobLogVO;
import org.example.rabbitmq.entity.RabbitMQList;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
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

    @Value("${mq-name}")
    private String mqName;

    @Value("${mq-interval:500}")
    private Long interval;

    @Value("${mq-thread:5}")
    private int thread;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final AtomicBoolean tag = new AtomicBoolean(false);

    private final AtomicInteger num = new AtomicInteger(0);

    @GetMapping("/sendMsg")
    @ApiOperation("发送消息")
    public String sendMsg(@RequestParam String msg) {
        rabbitTemplate.convertAndSend(mqName, msg);
        return "成功";
    }

    @GetMapping("/getMsgCount")
    @ApiOperation("获取剩余消息数量")
    public int getMsgCount() {
        AMQP.Queue.DeclareOk execute = rabbitTemplate.execute(channel -> channel.queueDeclarePassive(mqName));
        return execute.getMessageCount();
    }

    @GetMapping("/sendMsgTag")
    @ApiOperation("开启循环发送消息")
    public String sendMsgTag() {
        this.tag.set(!this.tag.get());
        this.num.set(0);
        return "成功";
    }

    @GetMapping("/getRabbitList")
    @ApiOperation("获取发送了，但未消费到的消息")
    public RabbitMQList getRabbitList() {
        RabbitMQList rabbitMQList = new RabbitMQList();
        rabbitMQList.setUnReceived(RabbitMQContext.getUnReceived());
        rabbitMQList.setUnFinish(RabbitMQContext.getUnFinish());
        return rabbitMQList;
    }

    @PostConstruct
    public void sendMsgRing() {
        log.info("开始发送消息");
        ExecutorService executorService = Executors.newFixedThreadPool(this.thread);
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (tag.get()) {
                    int jobId = num.incrementAndGet();
                    JobLogVO jobLogVO = getJobVO(jobId, 1);
                    rabbitTemplate.convertAndSend(mqName, "", jobLogVO);
                    jobLogVO.setStatus(2);
                    rabbitTemplate.convertAndSend(mqName, "", jobLogVO);
                    RabbitMQContext.sendList.add(jobId);
                }
            }
        });
        for (int i = 0; i < this.thread; i++) {
            executorService.submit(thread);
        }
    }

    public JobLogVO getJobVO(Integer jobId, Integer status) {
        JobLogVO logVO = new JobLogVO();
        logVO.setJobId(jobId);
        logVO.setLogPath("logDir");
        logVO.setName("logName");
        logVO.setEngineId(1);
        logVO.setSourceTableName("test1");
        logVO.setTargetTableName("test2");
        logVO.setStatus(status);
        logVO.setStrategy(1);
        logVO.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logVO.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return logVO;
    }

}

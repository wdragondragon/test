package org.example.rabbitmq.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.example.rabbitmq.context.RabbitMQContext;
import org.example.rabbitmq.entity.JobLogVO;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "logDetails-fanout-test-1"),
                    exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "${mq-name}"))
    }, concurrency = "1-100")
//    @RabbitListener(queuesToDeclare = @Queue("${mq-name}"), concurrency = "1-100")
    public void logDetails(JobLogVO jobLogVO) {
        if (log.isDebugEnabled()) {
            log.debug("消费1 id：[{}]，status：[{}]", jobLogVO.getJobId(), jobLogVO.getStatus());
        }
        if (Objects.equals(jobLogVO.getStatus(), 1)) {
            RabbitMQContext.runList.add(jobLogVO.getJobId());
        } else {
            RabbitMQContext.finishList.add(jobLogVO.getJobId());
        }
        List<Integer> unFinishList = RabbitMQContext.getUnFinish();
        log.info("剩余：{}", unFinishList.size());
    }

    /**
     * 消费日志
     */
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = "logDetails-fanout-test-2"),
                    exchange = @Exchange(type = ExchangeTypes.FANOUT, name = "${mq-name}"))
    }, concurrency = "1-100")
//    @RabbitListener(queuesToDeclare = @Queue("${mq-name}"), concurrency = "1-100")
    public void logDetails2(JobLogVO jobLogVO) {
        if (log.isDebugEnabled()) {
            log.debug("消费者2 id：[{}]，status：[{}]", jobLogVO.getJobId(), jobLogVO.getStatus());
        }
//        if (Objects.equals(jobLogVO.getStatus(), 1)) {
//            RabbitMQContext.runList.add(jobLogVO.getJobId());
//        } else {
//            RabbitMQContext.finishList.add(jobLogVO.getJobId());
//        }
//        List<Integer> unFinishList = RabbitMQContext.getUnFinish();
//        log.info("剩余：{}", unFinishList.size());
    }
}

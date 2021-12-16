package com.jdragon.springboot.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.09.07 13:52
 * @Description:
 */
@Component
@Slf4j
public class scheduledSocket {
    @Autowired
    WebSocketServer webSocketServer;

    @Scheduled(cron="0/5 * * * * ?")  //每天00:00触发执行
    @Async
    public void sendMessage(){
        WebSocketServer.getSessionPools().forEach((s, session) -> webSocketServer.sendMessage(session,s));
    }
}

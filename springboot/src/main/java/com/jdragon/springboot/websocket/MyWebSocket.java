package com.jdragon.springboot.websocket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.09.07 13:24
 * @Description:
 */

@ServerEndpoint(value = "/webSocket/test")
@Component
@Slf4j
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineNum = new AtomicInteger();

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    //发送消息
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }
    //给指定用户发送信息
    public void sendInfo(String userName, String message){
        for (MyWebSocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    //建立连接成功调用
    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        /**
         * 加入set中
         */
        webSocketSet.add(this);
        /**
         * 在线数加1
         */
        addOnlineCount();
        log.info("有新连接加入！当前在线人数为" + onlineNum);
        try {
            sendMessage("success");
        } catch (IOException e) {
            log.info("webSocket--IO异常");
        }
    }

    //关闭连接时调用
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + onlineNum);
    }

    //收到客户端信息
    @OnMessage
    public void onMessage(String message) {
        message = "客户端：" + message + ",已收到";
        System.out.println(message);
        for (MyWebSocket item: webSocketSet) {
            try {
                item.sendMessage(message);
            } catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        log.error("websocket：发生错误");
        throwable.printStackTrace();
    }

    public static void addOnlineCount(){
        onlineNum.incrementAndGet();
    }

    public static void subOnlineCount() {
        onlineNum.decrementAndGet();
    }
}

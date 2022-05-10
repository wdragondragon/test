package org.example.ftp.communication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author JDragon
 * @Date 2022.05.10 上午 9:20
 * @Email 1061917196@qq.com
 * @Des:
 */
public abstract class Reporter {

    protected static final Map<String, Communication> taskGroupCommunicationMap = new ConcurrentHashMap<>();

    protected final long START_TIME = System.currentTimeMillis();

    private boolean finish = false;

    public static void reg(String name, Communication communication) {
        taskGroupCommunicationMap.put(name, communication);
    }

    public abstract void reportAll();

    public abstract void report(Communication communication);

    public void finish() {
        finish = true;
    }

    public boolean isFinish() {
        return finish;
    }
}

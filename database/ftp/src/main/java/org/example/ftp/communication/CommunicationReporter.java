package org.example.ftp.communication;

import lombok.extern.slf4j.Slf4j;
import org.example.ftp.key.Key;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author JDragon
 * @Date 2022.05.06 上午 10:51
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class CommunicationReporter extends Thread {

    private static final Map<String, Communication> taskGroupCommunicationMap = new ConcurrentHashMap<>();

    private final static Map<Integer, String> unitMap = new HashMap<Integer, String>() {{
        put(1, "b");
        put(2, "kb");
        put(3, "mb");
        put(4, "gb");
    }};

    private static final long PRINT_TIME = 3 * 1000L;

    private static final long START_TIME = System.currentTimeMillis();

    @Override
    public void run() {
        log.info("传输信息上报启动");
        while (true) {
            try {
                Thread.sleep(PRINT_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean hasNoFinish = false;
            long allTransferSpeed = 0L;
            long allTransferCount = 0L;
            for (String name : taskGroupCommunicationMap.keySet()) {
                Communication communication = taskGroupCommunicationMap.get(name);
                if (communication.getStatus() == 1) {
                    this.reportNow(communication);
                    allTransferSpeed += communication.getNumber(Key.TRANSFER_SPEED).longValue();
                }
                if (!hasNoFinish && communication.getStatus() != 2) {
                    hasNoFinish = true;
                }
                allTransferCount += communication.getNumber(Key.ALL_TRANSFER_COUNT).longValue();
            }
            if (hasNoFinish) {
                log.info("传输总大小[{}]速度：[{}/s]", toUnit(allTransferCount), toUnit(allTransferSpeed));
            } else {
                allTransferSpeed = (allTransferCount / (System.currentTimeMillis() - START_TIME)) * 1000L;
                log.info("传输完成，传输总大小[{}]速度：[{}/s]", toUnit(allTransferCount), toUnit(allTransferSpeed));
                break;
            }
        }
    }

    public static void reg(String name, Communication communication) {
        taskGroupCommunicationMap.put(name, communication);
    }

    public void reportNow(Communication communication) {
        long now = System.currentTimeMillis();
        long count = communication.getNumber(Key.COUNT).longValue();
        long start = communication.getNumber(Key.START).longValue();
        long transferSize = communication.getNumber(Key.TRANSFER_SIZE).longValue();
        long transferCount = communication.getNumber(Key.TRANSFER_COUNT).longValue();
        long fileSize = communication.getNumber(Key.FILE_SIZE).longValue();
        long startTime = communication.getNumber(Key.START_TIME).longValue();
        long transferSpeed = (transferCount / (now - startTime)) * 1000L;
        communication.setNumber(Key.TRANSFER_SPEED, transferSpeed);
        try {
            log.info("[{}] [{}]-->[{}],进度[{}%],文件进度[{}%],[{}],速度：[{}/s]",
                    communication.getName(),
                    communication.getString(Key.SOURCE_PATH),
                    communication.getString(Key.TARGET_PATH),
                    (count - start) * 100 / transferSize,
                    count * 100 / fileSize,
                    toUnit(count),
                    toUnit(transferSpeed)
            );
        } catch (Exception e) {
            log.error("上报进度失败", e);
        }
    }

    public String toUnit(long size) {
        List<Integer> numList = new LinkedList<>();
        while (size != 0) {
            numList.add((int) (size & 1023));
            size = size >> 10;
        }
        StringBuilder progress = new StringBuilder();
        for (int i = numList.size() - 1; i >= 0; i--) {
            progress.append(numList.get(i)).append(unitMap.get(i + 1)).append(" ");
        }
        return progress.toString();
    }
}

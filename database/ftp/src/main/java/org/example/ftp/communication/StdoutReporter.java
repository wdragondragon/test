package org.example.ftp.communication;

import lombok.extern.slf4j.Slf4j;
import org.example.ftp.key.Key;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author JDragon
 * @Date 2022.05.10 上午 9:22
 * @Email 1061917196@qq.com
 * @Des:
 */

@Slf4j
public class StdoutReporter extends Reporter {
    private final static Map<Integer, String> unitMap = new HashMap<Integer, String>() {{
        put(1, "b");
        put(2, "kb");
        put(3, "mb");
        put(4, "gb");
    }};

    public void reportAll() {
        long allTransferSpeed = 0L;
        long allTransferCount = 0L;

        for (String name : taskGroupCommunicationMap.keySet()) {
            Communication communication = taskGroupCommunicationMap.get(name);
            if (communication.getStatus() == 1) {
                allTransferSpeed += this.computeTransferSpeed(communication);
                this.report(communication);
            }
            allTransferCount += communication.getNumber(Key.ALL_TRANSFER_COUNT).longValue();
        }

        boolean hasNoFinish = taskGroupCommunicationMap.values().stream()
                .anyMatch(communication -> communication.getStatus() != 2);

        if (hasNoFinish) {
            log.info("传输总大小[{}]速度：[{}/s]", toUnit(allTransferCount), toUnit(allTransferSpeed));
        } else {
            allTransferSpeed = (allTransferCount / (System.currentTimeMillis() - START_TIME)) * 1000L;
            log.info("传输完成，传输总大小[{}]速度：[{}/s]", toUnit(allTransferCount), toUnit(allTransferSpeed));
            finish();
        }
    }

    @Override
    public void report(Communication communication) {
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
            log.info("[{}] [{}]-->[{}] \n续传起始点 [{}] 进度[{}%],文件进度[{}%],[{}],速度：[{}/s],",
                    communication.getName(),
                    communication.getString(Key.SOURCE_PATH),
                    communication.getString(Key.TARGET_PATH),
                    toUnit(communication.getNumber(Key.SEQUEL_INDEX).longValue()),
                    (count - start) * 100 / transferSize,
                    count * 100 / fileSize,
                    toUnit(count),
                    toUnit(transferSpeed)
            );
        } catch (Exception e) {
            log.error("上报进度失败", e);
        }
    }

    public long computeTransferSpeed(Communication communication) {
        long now = System.currentTimeMillis();
        long transferCount = communication.getNumber(Key.TRANSFER_COUNT).longValue();
        long startTime = communication.getNumber(Key.START_TIME).longValue();
        return (transferCount / (now - startTime)) * 1000L;
    }

    public String toUnit(long size) {
        List<Integer> numList = new LinkedList<>();
        if (size == 0) {
            numList = Collections.singletonList(0);
        }
        while (size != 0) {
            numList.add((int) (size & 1023));
            size = size >> 10;
        }
        StringBuilder progress = new StringBuilder();
        for (int i = numList.size() - 1; i >= 0; i--) {
            progress.append(numList.get(i)).append(unitMap.get(i + 1)).append(" ");
        }
        return progress.toString().trim();
    }
}

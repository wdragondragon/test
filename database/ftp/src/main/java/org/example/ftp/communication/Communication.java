package org.example.ftp.communication;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author JDragon
 * @Date 2022.05.06 上午 10:11
 * @Email 1061917196@qq.com
 * @Des:
 */
@Slf4j
public class Communication {

    private final String name;

    private int status = 0;

    /**
     * 所有的数值key-value对 *
     */
    private final Map<String, Number> counter = new HashMap<>();

    private final Map<String, String> recorder = new HashMap<>();

    public Communication(String name) {
        this.name = name;
        ReportThread.reg(name, this);
        setNumber("lastReportTime", System.currentTimeMillis());
    }

    public void setNumber(String key, Number value) {
        counter.put(key, value);
    }

    public void setString(String key, String value) {
        recorder.put(key, value);
    }

    public void increaseLong(String key, long value) {
        Number number = counter.get(key);
        if (number == null) {
            number = 0L;
        }
        setNumber(key, number.longValue() + value);
    }

    public void increaseDouble(String key, double value) {
        Number number = counter.get(key);
        setNumber(key, number.doubleValue() + value);
    }

    public Number getNumber(String key) {
        return Optional.ofNullable(counter.get(key)).orElse(0L);
    }

    public String getString(String key) {
        return recorder.get(key);
    }


    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void finish() {
        this.status = 2;
    }

    public void start() {
        this.status = 1;
    }
}

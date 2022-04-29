package org.example.ftp;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * @Author JDragon
 * @Date 2022.04.29 下午 3:26
 * @Email 1061917196@qq.com
 * @Des:
 */
@Data
@Slf4j
public class FilePart {

    private Integer partId;

    private long start;

    private long end;

    private long size;

    private boolean finish = false;

    private CountDownLatch countDownLatch;

    private UploadStatus uploadStatus;

    public FilePart(Integer partId, long start, long end, CountDownLatch countDownLatch) {
        this.partId = partId;
        this.start = start;
        this.end = end;
        this.size = end - start;
        this.countDownLatch = countDownLatch;
    }

    public void finish(UploadStatus uploadStatus) {
        if (!this.finish) {
            log.info("[{}]完成传输", partId);
            this.countDownLatch.countDown();
            this.finish = true;
            this.uploadStatus = uploadStatus;
        }
    }
}

package org.example.ftp;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author JDragon
 * @Date 2022.04.29 下午 3:26
 * @Email 1061917196@qq.com
 * @Des:
 */
@Data
@Slf4j
public class FilePart {

    private long start;

    private long end;

    private long size;

    public FilePart(long start, long end) {
        this.start = start;
        this.end = end;
        this.size = end - start;
    }
}

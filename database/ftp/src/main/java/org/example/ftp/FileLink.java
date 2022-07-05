package org.example.ftp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.ftp.key.UploadStatus;

/**
 * @Author JDragon
 * @Date 2022.05.05 下午 7:19
 * @Email 1061917196@qq.com
 * @Des:
 */

@Getter
@ToString
@AllArgsConstructor
public class FileLink {

    private final String sourceFilePath;

    private final String targetFilePath;

    private final UploadStatus uploadStatus;
}

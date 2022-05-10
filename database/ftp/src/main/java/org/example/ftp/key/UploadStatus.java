package org.example.ftp.key;

import java.util.Arrays;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2022.04.27 下午 2:16
 * @Email 1061917196@qq.com
 * @Des:
 */
public enum UploadStatus {
    Source_not_exist(),
    Create_Directory_Fail(),
    File_Exits(),
    Remote_Bigger_Local(),
    Upload_From_Break_Success(),
    Delete_Remote_Faild(),
    Upload_New_File_Success(),
    Upload_New_File_Failed(),


    ;

    public static final List<UploadStatus> needFreshStatus = Arrays.asList(Upload_From_Break_Success, Upload_New_File_Success, Upload_New_File_Failed);
}

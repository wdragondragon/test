package org.example.ftp;

/**
 * @Author JDragon
 * @Date 2022.04.27 下午 2:16
 * @Email 1061917196@qq.com
 * @Des:
 */
public enum UploadStatus {
    Create_Directory_Fail(),
    File_Exits(),
    Remote_Bigger_Local(),
    Upload_From_Break_Success(),
    Delete_Remote_Faild(),
    Upload_New_File_Success(),
    Upload_New_File_Failed(),
}
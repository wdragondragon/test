package org.example.javabase.zip;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.example.javabase.sftp.ftp.FTP;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @Author: JDragon
 * @Data:2022/9/21 16:11
 * @Description:
 */
public class ZipTest {
    public static void main(String[] args) throws IOException {
        FTP ftp = new FTP("192.168.1.150", 21, "user_test", "zhjl951753");
        ftp.open();
        InputStream inputStream = ftp.getInputStream("/ftp_test/te.zip");


        String filePath = "D:\\Desktop\\te.zip";
        String targetPath = "D:\\Desktop\\test\\";
        java.util.zip.ZipFile zf = new java.util.zip.ZipFile(filePath);
//        InputStream in = new BufferedInputStream(new FileInputStream(filePath));
        InputStream in = new BufferedInputStream(inputStream);
        ZipInputStream zin = new ZipInputStream(in);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            String targetZipPath = targetPath + ze.getName().replaceAll("\\\\/", "\\");
            System.out.println(targetZipPath);
            File file = new File(targetZipPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            long size = ze.getSize();
            if (size > 0) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(zf.getInputStream(ze)));
                IoUtil.copy(br, new OutputStreamWriter(new FileOutputStream(file)));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
//                }
                br.close();
            }
        }
        zin.closeEntry();
    }
}

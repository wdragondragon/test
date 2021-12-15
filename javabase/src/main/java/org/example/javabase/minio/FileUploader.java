package org.example.javabase.minio;

import io.minio.messages.Item;

import java.io.*;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.07.08 上午 10:15
 * @Email 1061917196@qq.com
 * @Des:
 */
public class FileUploader {

    public static String bucketName = "test";

    public static void main(String[] args) throws Exception {
        Minio minio = Minio.builder()
                .endpoint("http://39.96.83.89:9000")
                .accessKey("jdragon")
                .secretKey("zhjl951753")
                .build();

        minio.init();

//        minio.getMinioClient().getObject(GetObjectArgs.builder().bucket(bucketName).object("test.csv").build());

        minio.makeBucket(bucketName);

        //通过文件上传
        minio.uploadFile(bucketName, "ids.txt", "C:\\Users\\10619\\Desktop\\ids.txt");

        //通过流上传
        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\10619\\Desktop\\ids.txt")) {
            minio.putObject(bucketName, "/ids/ids2.txt", fileInputStream);
        }

        //获取bucket文件列表
        minio.listObjects(bucketName).forEach(item -> System.out.println(item.objectName()));

        //下载文件，并输出内容
        try (InputStream inputStream = minio.getObject(bucketName, "ids.txt");
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                System.out.println(temp);
            }
        }

        //下载流
        try (FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\10619\\Desktop\\downIds.txt")) {
            minio.getObject(bucketName, "ids.txt", fileOutputStream);
        }

        //下载文件
        minio.downloadFile(bucketName, "ids.txt", "C:\\Users\\10619\\Desktop\\downIds2.txt");


        List<Item> items = minio.listObjects(bucketName,"ids");
        items.forEach(item -> System.out.println(item.objectName()));

        //删除文件
//        minio.removeObjects(bucketName, "ids.txt", "ids2.txt", "/var/ids.txt");
        minio.removeObjects(bucketName,"ids");

        items = minio.listObjects(bucketName,"/ids/");
        items.forEach(item -> System.out.println(item.objectName()));
    }
}

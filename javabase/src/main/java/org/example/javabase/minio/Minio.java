package org.example.javabase.minio;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.07.08 下午 2:21
 * @Email 1061917196@qq.com
 * @Des:
 */
@Builder
public class Minio {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    @Getter
    private MinioClient minioClient;

    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    /**
     * 判断存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建一个新的存储桶
     *
     * @param bucketName 新的存储桶名称
     */
    public void makeBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    /**
     * 获取存储桶下的所有对象
     *
     * @param bucketName 存储桶名称
     * @return 该存储桶的所有对象
     */
    public List<Item> listObjects(String bucketName) {
        return listObjects(bucketName, null);
    }

    public List<Item> listObjects(String bucketName, String prefix) {
        List<Item> listObjects = new LinkedList<>();
        Iterable<Result<Item>> itemIterable = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).build());
        for (Result<Item> itemResult : itemIterable) {
            try {
                Item item = itemResult.get();
                listObjects.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listObjects;
    }

    /**
     * 下载对象到本地文件
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param fileName   保存到本地文件路径
     */
    public void downloadFile(String bucketName, String objectName, String fileName) throws Exception {
        minioClient.downloadObject(DownloadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(fileName).build());
    }

    /**
     * 将本地文件上传到对象
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param fileName   要上传的本地文件路径
     */
    public void uploadFile(String bucketName, String objectName, String fileName) throws Exception {
        minioClient.uploadObject(UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(fileName).build());
    }

    /**
     * 将对象下载对流中
     *
     * @param bucketName   存储桶名称
     * @param objectName   对象名称
     * @param outputStream 下载到目标流
     */
    public void getObject(String bucketName, String objectName, OutputStream outputStream) throws Exception {
        try (InputStream inputStream = getObject(bucketName, objectName)) {
            byte[] b = new byte[1024];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, length);
            }
        }
    }

    /**
     * 获取下载对象流
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @return 对象流
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName).build());
    }

    /**
     * 从流上传对象
     *
     * @param bucketName  存储桶名称
     * @param objectName  对象名称
     * @param inputStream 上传流
     */
    public void putObject(String bucketName, String objectName, InputStream inputStream) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(inputStream, inputStream.available(), -1).build());
    }

    /**
     * 批量删除对象
     *
     * @param bucketName  存储桶
     * @param objectNames 对象名称数组
     */
    public void removeObjects(String bucketName, String... objectNames) {
        removeObjects(bucketName, Arrays.asList(objectNames));
    }

    /**
     * 批量删除对象
     *
     * @param bucketName  存储桶
     * @param objectNames 删除对象名称链表
     */
    public void removeObjects(String bucketName, List<String> objectNames) {
        List<DeleteObject> objects = objectNames.stream().map(DeleteObject::new).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(objects).build());
        for (Result<DeleteError> result : results) {
            try {
                DeleteError error = result.get();
                System.out.println("Error in deleting object " + error.objectName() + "; " + error.message());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeObjects(String bucketName, String prefix) {
        List<Item> items = listObjects(bucketName, prefix);
        List<String> objectNames = items.stream().map(Item::objectName).collect(Collectors.toList());
        removeObjects(bucketName, objectNames);
    }
}

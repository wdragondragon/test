package org.example.ftp;

import com.alibaba.fastjson.JSONObject;
import org.example.ftp.communication.CommunicationReporter;
import org.example.ftp.file.FTPFileRecord;
import org.example.ftp.file.FileRecord;
import org.example.ftp.file.LocalFileRecord;
import org.example.ftp.file.SFTPFileRecord;
import org.example.ftp.helper.FileHelperFactory;
import org.example.ftp.key.ConfigKey;
import org.example.ftp.helper.FTPClientCloseable;
import org.example.ftp.helper.FileHelper;
import org.example.ftp.helper.SFTPClientCloseable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author JDragon
 * @Date 2022.04.27 下午 2:12
 * @Email 1061917196@qq.com
 * @Des:
 */
public class FileTransfer {

    private static final Logger LOG = LoggerFactory.getLogger(FileTransfer.class);

    private int transformerThread = 3;

    public static void main(String[] args) throws IOException {
        FileTransfer fileTransfer = new FileTransfer();
        Configuration configuration = Configuration.init(new File("C:\\Users\\10619\\Desktop\\config.json"));
        Configuration jobConfig = configuration.getConfig(ConfigKey.JOB_PARAMETER);
        fileTransfer.init(jobConfig);
        fileTransfer.start(jobConfig, configuration.getConfig(ConfigKey.READER_PARAMETER), configuration.getConfig(ConfigKey.WRITER_PARAMETER));
    }

    public void md5() {
        try (FTPClientCloseable targetFtp = new FTPClientCloseable("10.194.188.77", 21, "user_test", "EhnkvrX1V20=");
             SFTPClientCloseable sftpClientCloseable = new SFTPClientCloseable("10.194.188.77", 22, "root", "EhnkvrX1V20=");) {
            FileRecord sourceSFTPFileRecord = new SFTPFileRecord(sftpClientCloseable, "/data/ftp/user_test/test-local-sftp.jar");
            FileRecord targetFTPFileRecord = new FTPFileRecord(targetFtp, "test-sftp-ftp.jar");
            FileRecord sourceLocalFileRecord = new LocalFileRecord("C:\\Users\\10619\\Desktop\\test.jar");

            final String s = sourceLocalFileRecord.md5();
            final String s1 = sourceSFTPFileRecord.md5();
            final String s2 = targetFTPFileRecord.md5();
            System.out.println(s);
            System.out.println(s1);
            System.out.println(s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init(Configuration configuration) {
        this.transformerThread = configuration.get(ConfigKey.TRANSFORMER_THREAD, Integer.class);
    }

    public void start(Configuration jobConfig, Configuration sourceConfig, Configuration targetConfig) {
        String sourcePath = sourceConfig.get(ConfigKey.PATH, String.class);
        String targetPath = targetConfig.get(ConfigKey.PATH, String.class);
        Queue<String> fileLinkQueue = new ConcurrentLinkedQueue<>();
        try (FileHelper sourceFileHelper = FileHelperFactory.create(sourceConfig)) {
            Set<String> strings = sourceFileHelper.listFile(sourcePath, sourceConfig.get(ConfigKey.REGEX, String.class));
            fileLinkQueue.addAll(strings);
        } catch (Exception e) {
            LOG.error("获取源文件失败", e);
            return;
        }

        ExecutorService exec = Executors.newFixedThreadPool(transformerThread);
        CountDownLatch latch = new CountDownLatch(transformerThread);
        List<FileTransferCustomer> transferCustomerList = new LinkedList<>();
        LOG.info("文件传输线程初始化");
        for (int i = 0; i < transformerThread; i++) {
            try {
                FileHelper sourceFileHelper = FileHelperFactory.create(sourceConfig);
                FileHelper targetFileHelper = FileHelperFactory.create(targetConfig);
                FileTransferCustomer fileTransferCustomer = FileTransferCustomer.builder()
                        .num(i)
                        .sourceFileHelper(sourceFileHelper).sourcePath(sourcePath)
                        .targetFileHelper(targetFileHelper).targetPath(targetPath)
                        .fileRecordConcurrentLinkedQueue(fileLinkQueue).countDownLatch(latch)
                        .retryTimes(jobConfig.get(ConfigKey.RETRY_TIMES, Integer.class)).transferBuffer(jobConfig.get(ConfigKey.REPORT_INTERVAL, Integer.class))
                        .build();
                transferCustomerList.add(fileTransferCustomer);
            } catch (Exception e) {
                LOG.error("连接失败", e);
            }
        }

        new CommunicationReporter().start();

        LOG.info("文件传输线程启动");
        for (FileTransferCustomer fileTransferCustomer : transferCustomerList) {
            LOG.info("文件传输线程[{}]启动", fileTransferCustomer.getName());
            exec.submit(fileTransferCustomer);
        }

        try {
            latch.await();
            exec.shutdown();
            LOG.info("文件传输全部完成");
        } catch (InterruptedException e) {
            LOG.error("latch等待异常", e);
        }

        List<FileLink> transferResult = new LinkedList<>();
        for (FileTransferCustomer fileTransferCustomer : transferCustomerList) {
            transferResult.addAll(fileTransferCustomer.getResult());
        }
        LOG.info("文件传输结果[{}]", JSONObject.toJSONString(transferResult));
    }
}
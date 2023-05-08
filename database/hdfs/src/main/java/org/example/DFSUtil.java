package org.example;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class DFSUtil {

    private final Configuration hadoopConf;

    private final String kerberosKeytabFilePath;

    private final String kerberosPrincipal;

    private final String krb5Conf;

    public static final String HDFS_DEFAULTFS_KEY = "fs.defaultFS";

    public static final String HADOOP_SECURITY_AUTHENTICATION_KEY = "hadoop.security.authentication";

    public static final String JAVA_SECURITY_KRB5_CONF_KEY = "java.security.krb5.conf";

    public DFSUtil(String defaultFs, boolean haveKerberos, String hdfsSiteFile, String coreSiteFile) {
        this.kerberosKeytabFilePath = "";
        this.kerberosPrincipal = "";
        this.krb5Conf = "";

        hadoopConf = new org.apache.hadoop.conf.Configuration();
        //io.file.buffer.size 性能参数
        //http://blog.csdn.net/yangjl38/article/details/7583374
        if (StringUtils.isNotBlank(defaultFs)) {
            hadoopConf.set(HDFS_DEFAULTFS_KEY, defaultFs);
        }
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

        if (StringUtils.isBlank(hdfsSiteFile)) {
            log.warn("hdfs-site.xml not set");
        } else {
            log.info("load hdfs-site.xml at {}", hdfsSiteFile);
            hadoopConf.addResource(new Path(hdfsSiteFile));
        }

        if (StringUtils.isBlank(coreSiteFile)) {
            log.warn("core-site.xml not set");
        } else {
            log.info("load core-site.xml at {}", coreSiteFile);
            hadoopConf.addResource(new Path(coreSiteFile));
        }
        //是否有Kerberos认证
        if (haveKerberos) {
            log.info("haveKerberos is true");
            this.hadoopConf.set(HADOOP_SECURITY_AUTHENTICATION_KEY, "kerberos");
            this.hadoopConf.set("hadoop.security.authorization", "true");
        }
        String s = this.hadoopConf.get(HADOOP_SECURITY_AUTHENTICATION_KEY);
        log.info(String.format("hadoop.security.authentication:%s", s));
        log.info(String.format("hadoopConfig details:%s", JSON.toJSONString(this.hadoopConf)));
    }

    public DFSUtil(String defaultFs, boolean haveKerberos, String kerberosKeytabFilePath, String kerberosPrincipal, String krb5Conf, String hdfsSiteFile, String coreSiteFile) {
        this.kerberosKeytabFilePath = kerberosKeytabFilePath;
        this.kerberosPrincipal = kerberosPrincipal;
        this.krb5Conf = krb5Conf;

        hadoopConf = new org.apache.hadoop.conf.Configuration();
        //io.file.buffer.size 性能参数
        //http://blog.csdn.net/yangjl38/article/details/7583374
        if (StringUtils.isNotBlank(defaultFs)) {
            hadoopConf.set(HDFS_DEFAULTFS_KEY, defaultFs);
        }
        hadoopConf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");


        if (StringUtils.isBlank(hdfsSiteFile)) {
            log.warn("hdfs-site.xml not set");
        } else {
            log.info("load hdfs-site.xml at {}", hdfsSiteFile);
            hadoopConf.addResource(new Path(hdfsSiteFile));
        }

        if (StringUtils.isBlank(coreSiteFile)) {
            log.warn("core-site.xml not set");
        } else {
            log.info("load core-site.xml at {}", coreSiteFile);
            hadoopConf.addResource(new Path(coreSiteFile));
        }

        //是否有Kerberos认证
        if (haveKerberos) {
            this.hadoopConf.set(HADOOP_SECURITY_AUTHENTICATION_KEY, "kerberos");
            System.setProperty(JAVA_SECURITY_KRB5_CONF_KEY, this.krb5Conf);
            try {
                this.initHadoopSecurity(hadoopConf);
            } catch (IOException e) {
                throw new RuntimeException("kerberos认证失败", e);
            }
        }

        log.info(String.format("hadoopConfig details:%s", JSON.toJSONString(this.hadoopConf)));
    }

    public void initHadoopSecurity(Configuration conf) throws IOException {
        LoginUtil.setJaasConf("Client", this.kerberosPrincipal, this.kerberosKeytabFilePath);
        LoginUtil.setZookeeperServerPrincipal("zookeeper.server.principal", "zookeeper/hadoop");
        LoginUtil.login(this.kerberosPrincipal, this.kerberosKeytabFilePath, this.krb5Conf, conf);
    }

    public static FileStatus fileStatus(FileSystem fileSystem, String path) throws IOException {
        return fileSystem.getFileStatus(new Path(path));
    }

    public static Set<String> getFileList(FileSystem fileSystem, String path) throws IOException {
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
        return Arrays.stream(fileStatuses).map(FileStatus::getPath).map(Path::toString).collect(Collectors.toSet());
    }

    public FileSystem getFileSystem() throws IOException {
        return FileSystem.get(hadoopConf);
    }
}

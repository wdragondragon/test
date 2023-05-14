package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.security.auth.callback.TextCallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.example.kerberos.GetKerberosObject;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Set;

/**
 * Hello world!
 */
@Slf4j
public class Krb {

    public static final String HDFS_DEFAULTFS_KEY = "fs.defaultFS";

    public static final String HADOOP_SECURITY_AUTHENTICATION_KEY = "hadoop.security.authentication";

    public static final String KERBEROS_FILE_PATH = "D:\\dev\\IdeaProjects\\test\\database\\hdfs\\src\\main\\resources\\";

    public static final String DEFAULT_HDFS_SITE_PATH = KERBEROS_FILE_PATH + "hdfs-site.xml";

    public static final String DEFAULT_CORE_SITE_PATH = KERBEROS_FILE_PATH + "core-site.xml";

    public static final String DEFAULT_KEYTAB_PATH = KERBEROS_FILE_PATH + "user.keytab";

    public static final String DEFAULT_KRB5_PATH = KERBEROS_FILE_PATH + "krb5.conf";

    public static void main(String[] args) throws Exception {
        String defaultFS = "hdfs://hacluster";

        String hdfsSiteFile = System.getProperty("hdfssite", DEFAULT_HDFS_SITE_PATH);
        if (StringUtils.isBlank(hdfsSiteFile)) {
            log.warn("hdfs-site.xml not set");
            hdfsSiteFile = DEFAULT_HDFS_SITE_PATH;
        }
        String coreSiteFile = System.getProperty("coresite", DEFAULT_CORE_SITE_PATH);
        if (StringUtils.isBlank(coreSiteFile)) {
            log.warn("core-site.xml not set");
            coreSiteFile = DEFAULT_CORE_SITE_PATH;
        }
        String kerberosKeytabFilePath = System.getProperty("keytab", DEFAULT_KEYTAB_PATH);
        String kerberosPrincipal = System.getProperty("user", "wedata_poc@HADOOP_DI.COM");
        String krb5Conf = System.getProperty("java.security.krb5.conf");
        if (StringUtils.isBlank(krb5Conf)) {
            krb5Conf = DEFAULT_KRB5_PATH;
            System.setProperty("java.security.krb5.conf", krb5Conf);
        }
        Boolean haveKerberos = "true".equals(System.getProperty("haveKerberos", "false"));


        org.apache.hadoop.conf.Configuration hadoopConf;
        hadoopConf = new org.apache.hadoop.conf.Configuration();

        if (StringUtils.isBlank(defaultFS)) {
            hadoopConf.set(HDFS_DEFAULTFS_KEY, defaultFS);
        }

        log.info("load hdfs-site.xml at {}", hdfsSiteFile);
        hadoopConf.addResource(new Path(hdfsSiteFile));

        log.info("load core-site.xml at {}", coreSiteFile);
        hadoopConf.addResource(new Path(coreSiteFile));

        //是否有Kerberos认证
        // Kerberos
        if (haveKerberos) {
            hadoopConf.set(HADOOP_SECURITY_AUTHENTICATION_KEY, "kerberos");
            log.info("host:{}\n, userKeytabFile:{}\n, userHdfsSiteFileName:{}\n, userCoreSiteFileName:{}\n, userPrincipal:{}\n",
                    defaultFS, kerberosKeytabFilePath, hdfsSiteFile, coreSiteFile, kerberosPrincipal);
        }
        GetKerberosObject getKerberosObject = new GetKerberosObject(kerberosPrincipal, kerberosKeytabFilePath, krb5Conf, hadoopConf, haveKerberos);
        try {
            FileSystem fs = getKerberosObject.doAs(() -> FileSystem.get(hadoopConf));
            FileStatus fileStatus = fs.getFileStatus(new Path("/"));
            System.out.println(fileStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
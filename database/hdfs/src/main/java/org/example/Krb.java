package org.example;

import com.sun.security.auth.callback.TextCallbackHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;

import javax.security.auth.Subject;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;

/**
 * Hello world!
 */
@Slf4j
public class Krb {
    public static void main(String[] args) {
        String host = "hdfs://hacluster";
        String userKeytabFile = System.getProperty("keytab", "/data/bmdata/software/hdfs/user.keytab");
        String userHdfsSiteFileName = System.getProperty("hdfssite", "/data/bmdata/software/hdfs/hdfs-site.xml");
        String userCoreSiteFileName = System.getProperty("coresite", "/data/bmdata/software/hdfs/core-site.xml");
        String userPrincipal = System.getProperty("user", "wedata_poc@HADOOP_DI.COM");
        log.info("host:{}\n, userKeytabFile:{}\n, userHdfsSiteFileName:{}\n, userCoreSiteFileName:{}\n, userPrincipal:{}\n",
                host, userKeytabFile, userHdfsSiteFileName, userCoreSiteFileName, userPrincipal);
        try {
            Krb krb = new Krb();
            FileSystem fs = krb.getFS(host, userHdfsSiteFileName, userCoreSiteFileName, userKeytabFile, userPrincipal);
            FileStatus fileStatus = DFSUtil.fileStatus(fs, "/");
            System.out.println(fileStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private FileSystem getFS(String host, String userHdfsSiteFileName, String userCoreSiteFileName, String userKeytabFile, String principal) throws IOException {
        String krb5File = System.getProperty("java.security.krb5.conf");
        if (StringUtils.isBlank(krb5File)) {
            krb5File = "/data/bmdata/software/hdfs/krb5.conf";
            System.setProperty("java.security.krb5.conf", krb5File);
        }
        log.info("krb5File:{}", krb5File);

        log.info("use new kerberos auth");
        try {
            return getFSKrb5(host, principal, userKeytabFile, userHdfsSiteFileName, userCoreSiteFileName);
        } catch (Exception e) {
            log.error("getDFSKrb5 error", e);
            throw new RuntimeException(e);
        }
    }

    private FileSystem getFSKrb5(String host, String principal, String userKeytabFile, String userHdfsSiteFileName, String userCoreSiteFileName) throws Exception {
        HashMap<String, String> krb5Options = new HashMap<>();
        krb5Options.put("principal", principal);
        krb5Options.put("keyTab", userKeytabFile);
        krb5Options.put("useKeyTab", "true");
        krb5Options.put("useTicketCache", "false");
        krb5Options.put("refreshKrb5Config", "true"); //临时刷新，并发时也有可能出问题
        krb5Options.put("debug", "false");
        krb5Options.put("doNotPrompt", "true");
        krb5Options.put("storeKey", "true");
        Configuration config = new Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
                return new AppConfigurationEntry[]{
                        new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
                                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, krb5Options)};
            }
        };

        LoginContext lc = new LoginContext("SampleClient", null, new TextCallbackHandler(), config);
        lc.login();
        log.info("Kerberos login success, " + lc.getSubject());
        return Subject.doAs(lc.getSubject(), (PrivilegedExceptionAction<FileSystem>) () -> {
            DFSUtil dfsUtil = new DFSUtil(host, true, userHdfsSiteFileName, userCoreSiteFileName);
            return dfsUtil.getFileSystem();
        });
    }

}

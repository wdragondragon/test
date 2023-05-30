package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JDragon
 * @date 2023/5/11 17:42
 * @description
 */
@Slf4j
public class GetKerberosObject {

    private final String principal;

    private final String userKeytabFile;

    private final String krb5Conf;

    private final Configuration configuration;

    private final Map<String, UserGroupInformation> kerberosCache = new HashMap<>();

    private final Boolean hasKerberos;

    public GetKerberosObject(String principal, String userKeytabFile, String krb5Conf, Configuration configuration, Boolean hasKerberos) {
        // 设置用户主体(Principal)
        configuration.set("hbase.client.kerberos.principal", principal);
        configuration.set("hbase.client.keytab.file", userKeytabFile);
        configuration.set("hbase.zookeeper.property.kerberos.serviceName", "zookeeper/hadoop.hadoop.com");
        handlZkSslEnabled(configuration);
        this.configuration = configuration;
        this.principal = principal;
        this.userKeytabFile = userKeytabFile;
        this.krb5Conf = krb5Conf;
        this.hasKerberos = hasKerberos;
    }

    /**
     * Properties for enabling encrypted HBase ZooKeeper communication
     */
    private static final String ZK_CLIENT_CNXN_SOCKET = "zookeeper.clientCnxnSocket";

    private static final String ZK_CLIENT_SECURE = "zookeeper.client.secure";

    private static final String ZK_SSL_SOCKET_CLASS = "org.apache.zookeeper.ClientCnxnSocketNetty";

    private void handlZkSslEnabled(Configuration conf) {
        boolean zkSslEnabled = conf.getBoolean("HBASE_ZK_SSL_ENABLED", false);
        if (zkSslEnabled) {
            System.setProperty(ZK_CLIENT_CNXN_SOCKET, ZK_SSL_SOCKET_CLASS);
            System.setProperty(ZK_CLIENT_SECURE, "true");
        } else {
            if (System.getProperty(ZK_CLIENT_CNXN_SOCKET) != null) {
                System.clearProperty(ZK_CLIENT_CNXN_SOCKET);
            }
            if (System.getProperty(ZK_CLIENT_SECURE) != null) {
                System.clearProperty(ZK_CLIENT_SECURE);
            }
        }
    }


    public <T> T doAs(PrivilegedExceptionAction<T> privilegedExceptionAction) throws Exception {
        if (!hasKerberos) {
            return privilegedExceptionAction.run();
        }
        UserGroupInformation userGroupInformation = this.initHadoopSecurity(principal, userKeytabFile, krb5Conf, configuration);
        log.info("Kerberos login success, " + userGroupInformation.toString());
        return userGroupInformation.doAs(privilegedExceptionAction);
    }

    public void doAs(PrivilegedExceptionActionNoResult privilegedExceptionAction) throws Exception {
        if (!hasKerberos) {
            privilegedExceptionAction.run();
            return;
        }
        UserGroupInformation userGroupInformation = this.initHadoopSecurity(principal, userKeytabFile, krb5Conf, configuration);
        log.info("Kerberos login success, " + userGroupInformation.toString());
        doAs(() -> {
            privilegedExceptionAction.run();
            return null;
        });
    }

    public UserGroupInformation initHadoopSecurity(String kerberosPrincipal, String kerberosKeytabFilePath, String krb5Conf, Configuration conf) throws IOException {
//        if (kerberosCache.containsKey(kerberosPrincipal + "-" + kerberosKeytabFilePath)) {
//            return kerberosCache.get(kerberosPrincipal + "-" + kerberosKeytabFilePath);
//        }
        LoginUtil.setJaasConf("Client", kerberosPrincipal, kerberosKeytabFilePath);
        LoginUtil.setZookeeperServerPrincipal("zookeeper.server.principal", "zookeeper/hadoop.hadoop.com");
        UserGroupInformation login = LoginUtil.login(kerberosPrincipal, kerberosKeytabFilePath, krb5Conf, conf);
//        kerberosCache.put(kerberosPrincipal + "-" + kerberosKeytabFilePath, login);
        return login;
    }

    @FunctionalInterface
    public interface PrivilegedExceptionActionNoResult {
        void run() throws Exception;
    }
}

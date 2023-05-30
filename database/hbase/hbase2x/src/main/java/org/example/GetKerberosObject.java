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
        this.configuration = configuration;
        this.principal = principal;
        this.userKeytabFile = userKeytabFile;
        this.krb5Conf = krb5Conf;
        this.hasKerberos = hasKerberos;
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
            return new Object();
        });
    }

    public UserGroupInformation initHadoopSecurity(String kerberosPrincipal, String kerberosKeytabFilePath, String krb5Conf, Configuration conf) throws IOException {
        String loginContextName = "Client";
        if (kerberosCache.containsKey(loginContextName)) {
            return kerberosCache.get(loginContextName);
        }
        LoginUtil.setJaasConf(loginContextName, kerberosPrincipal, kerberosKeytabFilePath);
//        LoginUtil.setJaasConf(kerberosPrincipal + "-" + kerberosKeytabFilePath, kerberosPrincipal, kerberosKeytabFilePath);
//        LoginUtil.setZookeeperServerPrincipal("zookeeper.server.principal", "zookeeper/hadoop");
        UserGroupInformation login = LoginUtil.login(kerberosPrincipal, kerberosKeytabFilePath, krb5Conf, conf);
        kerberosCache.put(loginContextName, login);
        return login;
    }

    @FunctionalInterface
    public interface PrivilegedExceptionActionNoResult {
        void run() throws Exception;
    }
}

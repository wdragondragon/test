package org.example.ftp.helper;

import org.example.ftp.Configuration;
import org.example.ftp.key.ConfigKey;

/**
 * @Author JDragon
 * @Date 2022.05.06 下午 5:20
 * @Email 1061917196@qq.com
 * @Des: 工厂
 */
public class FileHelperFactory {
    public static FileHelper create(Configuration config) throws Exception {
        String type = config.get(ConfigKey.TYPE, String.class);
        if (type.equals("ftp")) {
            return new FTPClientCloseable(
                    config.get(ConfigKey.HOST, String.class),
                    config.get(ConfigKey.PORT, Integer.class),
                    config.get(ConfigKey.USERNAME, String.class),
                    config.get(ConfigKey.PASSWORD, String.class));
        } else {
            return new SFTPClientCloseable(
                    config.get(ConfigKey.HOST, String.class),
                    config.get(ConfigKey.PORT, Integer.class),
                    config.get(ConfigKey.USERNAME, String.class),
                    config.get(ConfigKey.PASSWORD, String.class));
        }
    }
}

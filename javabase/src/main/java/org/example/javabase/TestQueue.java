package org.example.javabase;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author JDragon
 * @Date 2021.12.15 上午 11:53
 * @Email 1061917196@qq.com
 * @Des:
 */
public class TestQueue {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
    }
}

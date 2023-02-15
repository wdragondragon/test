package org.example;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Hello world!
 */
public class OracleTest {
    public static void main(String[] args) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = System.getProperty("url", "jdbc:oracle:thin:@192.168.1.150:1521:helowin");
        String username = System.getProperty("username", "test");
        String passWord = System.getProperty("password", "test");
        System.out.println(url);
        System.out.println(username);
        System.out.println(passWord);
        Connection con = DriverManager.getConnection(url, username, passWord);
        System.out.println("数据库连接成功。");
    }
}

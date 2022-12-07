package org.example.pg;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Author JDragon
 * @Date 2022/10/31 16:05
 * @description:
 */
public class PgTest {
    public static void main(String[] args) throws Exception {
        String url = System.getProperty("url");
        Class.forName("org.postgresql.Driver");
        if (url == null) {
            url = "jdbc:postgresql://%s:%s/%s";
        }
        String usrname = System.getProperty("username");
        String passWord = System.getProperty("password");
        Connection con = DriverManager.getConnection(url, usrname, passWord);
        System.out.println("数据库连接成功。");
    }
}

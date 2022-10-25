package org.example.sqlserver2008;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Author JDragon
 * @Date 2021.07.30 上午 10:07
 * @Email 1061917196@qq.com
 * @Des:
 */
public class SqlServer2008Test {
    public static void main(String[] args) throws Exception {
        String url = System.getProperty("url");
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        if(url==null){
            url = "jdbc:sqlserver://172.22.160.162:1433;DatabaseName=test";
        }
        String usrname = System.getProperty("username");
        String passWord = System.getProperty("password");
        Connection con = DriverManager.getConnection(url,usrname,passWord);
        System.out.println("数据库连接成功。");
    }
}

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
        //192.168.75.128
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://172.22.160.162:1433;DatabaseName=test";
        String usrname = "sa";
        String passWord = "zhjl951753";
        Connection con = DriverManager.getConnection(url,usrname,passWord);
        System.out.println("数据库连接成功。");
    }
}

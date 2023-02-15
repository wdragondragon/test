package org.example.mysql8;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @Author JDragon
 * @Date 2022/10/31 10:15
 * @description:
 */
public class App {
    public static void main(String[] args) throws Exception {
        String url = System.getProperty("url");
        Class.forName("com.mysql.jdbc.Driver");
        if(url==null){
            url = "jdbc:mysql://10.15.6.45:3306/partybuild";
        }
        String usrname = System.getProperty("username");
        String passWord = System.getProperty("password");
        Connection con = DriverManager.getConnection(url,usrname,passWord);
        System.out.println("数据库连接成功。");
    }
}

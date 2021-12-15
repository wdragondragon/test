package org.example.javabase.baseTest.jdbc;

import java.sql.*;

public class Conn {
    static Connection getConnection(){
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //连接数据库的url，test是我自己的一个数据库啊宝宝们。
            String url = "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8";
            //mysql登录名
            String user = "root";
            //mysql登录密码
            String pass = "951753";
            con = DriverManager.getConnection(url, user, pass);
            System.out.print("数据库连接成功\r");
        }catch(Exception e){
            e.printStackTrace();
        }
        return con;
    }
    public static PreparedStatement getPtmt(Connection con, String sql){
        PreparedStatement ptmt = null;
        try {
            ptmt = con.prepareStatement(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ptmt;
    }
    public static ResultSet getStmtSet(Connection con, String sql){
        ResultSet rs = null;
        try {
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }
    public static void execute(Connection con,String sql){
        try{
            Statement stmt = con.createStatement();
            stmt.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

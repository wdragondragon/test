package org.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 */
public class App {
    private static final String driverName = "com.mysql.jdbc.Driver";
//    private static final String url = "jdbc:mysql://39.96.83.89:3308/data_center";

    private static final String url = "jdbc:mysql://10.194.188.76:31369/data_center?useOldAliasMetadataBehavior=true";

    private static Connection con = null;
    private static Statement state = null;
    private static ResultSet res = null;

    @Before
    public void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        con = DriverManager.getConnection(url, "root", "Bmsoft1234");
        state = con.createStatement();
    }


    @Test
    public void insert() throws SQLException, FileNotFoundException {
//        FileReader fileReader = new FileReader("classpath:1.init-data.sql");
//        String s = fileReader.readString();
//        state.execute(s);
        System.out.println("连接成功");
    }

    // 释放资源
    @After
    public void destory() throws SQLException {
        if (res != null) state.close();
        if (state != null) state.close();
        if (con != null) con.close();
    }
}

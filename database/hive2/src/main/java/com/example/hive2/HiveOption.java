package com.example.hive2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author JDragon
 * @Date 2022.03.15 下午 3:58
 * @Email 1061917196@qq.com
 * @Des:
 */
public class HiveOption {
    private static final String driverName = "org.apache.hive.jdbc.HiveDriver";
    private static final String url = "jdbc:hive2://10.194.188.95:10000/wsb_test";


    private static Connection con = null;
    private static Statement state = null;
    private static ResultSet res = null;

    @Before
    public void init() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        con = DriverManager.getConnection(url, "hive", "hive");
        state = con.createStatement();
    }


    @Test
    public void test() throws SQLException {
        con.setAutoCommit(false);
        state.setFetchSize(Integer.MIN_VALUE);
        ResultSet resultSet = state.executeQuery("select * from wsb_test.dwd_xs_xsjbsjzlb");
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }


    @Test
    public void insert() throws SQLException {
        state.execute("INSERT INTO dwd_xs_xsjbsjzlb partition(dt_='20220315') \n" +
                "(xh,xm,ywxm,xmpy,cym,xbm,csrq,csdm,jg,mzm,gjdqm,sfzjlxm,sfzjh,hyzkm,gatqwm,zzmmm,jkzkm,xyzjm,xxm,zp,sfzjyxq,sfdszn,jtzz,jtyzbm,jtdh,jtdzxx) " +
                "VALUES " +
                "('xh','xm','ywxm','xmpy','cym','xbm','csrq','csdm','jg','mzm','gjdqm','sfzjlxm','sfzjh','hyzkm','gatqwm','zzmmm','jkzkm','xyzjm','xxm','zp','sfzjyxq','sfdszn','jtzz','jtyzbm','jtdh','jtdzxx')," +
                "('xh','xm','ywxm','xmpy','cym','xbm','csrq','csdm','jg','mzm','gjdqm','sfzjlxm','sfzjh','hyzkm','gatqwm','zzmmm','jkzkm','xyzjm','xxm','zp','sfzjyxq','sfdszn','jtzz','jtyzbm','jtdh','jtdzxx')," +
                "('xh','xm','ywxm','xmpy','cym','xbm','csrq','csdm','jg','mzm','gjdqm','sfzjlxm','sfzjh','hyzkm','gatqwm','zzmmm','jkzkm','xyzjm','xxm','zp','sfzjyxq','sfdszn','jtzz','jtyzbm','jtdh','jtdzxx')");

    }

    // 释放资源
    @After
    public void destory() throws SQLException {
        if (res != null) state.close();
        if (state != null) state.close();
        if (con != null) con.close();
    }


}

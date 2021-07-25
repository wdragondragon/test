package org.example.baseTest.jdbc.pg;

import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.20 11:18
 * @Description:
 */
@Slf4j
@Data
public class PgConn {
    private Connection connect;

    private final String url;

    private final String userName;

    private final String password;


    public PgConn(String url,String userName,String password){
        this.url = url;
        this.userName = userName;
        this.password = password;
        connect();
    }

    /**
     * 获取数据库连接
     **/
    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");
            //获取数据库连接
            connect = DriverManager.getConnection(url, userName, password);

        } catch (ClassNotFoundException e) {
            log.error("加载驱动失败！");
            e.printStackTrace();
        } catch (SQLException e) {
            log.error("获取数据库连接失败！");
            e.printStackTrace();
        }
    }
    public void execute(String sql){
        try{
            Statement stmt = connect.createStatement();
            stmt.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql){
        ResultSet rs = null;
        try {
            Statement stmt = connect.createStatement();
            rs = stmt.executeQuery(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
        return rs;
    }
}

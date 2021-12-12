package org.example.gbase8a;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author JDragon
 * @Date 2021.11.23 下午 2:58
 * @Email 1061917196@qq.com
 * @Des:
 */
public class GBase8aTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        GBase8aTest kingBaseTest = new GBase8aTest();
        Connection connect = kingBaseTest.getConnect();
        kingBaseTest.createTable();
        kingBaseTest.insert("张三");
        kingBaseTest.query();
        connect.close();
    }

    public static final String url = "jdbc:gbase://10.194.188.97:5258/test";  //改为自己数据库地址和名字
    public static final String name = "com.gbase.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "root";  //这里的用户名和密码设置为自己的
    public Connection conn = null;

    public Connection getConnect() throws ClassNotFoundException, SQLException {
        Class.forName(name);//指南中的这个方法运行不成功
        conn = DriverManager.getConnection(url, user, password);//获取连接
        return conn;
    }

    public void createTable() throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS gbase_test(\n" +
                "id int(11) primary key AUTO_INCREMENT,\n" +
                "name VARCHAR(255) not null\n" +
                ");");
        statement.close();
    }

    public void insert(String name) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO gbase_test (name) VALUES(?);");
        preparedStatement.setString(1, name);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void query() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM gbase_test;");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1) + ":" + resultSet.getString(2));
        }
        resultSet.close();
        statement.close();
    }
}

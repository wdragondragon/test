package org.example.kingbase8;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author JDragon
 * @Date 2021.11.22 下午 4:08
 * @Email 1061917196@qq.com
 * @Des:
 */
public class KingBaseTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        KingBaseTest kingBaseTest = new KingBaseTest();
        Connection connect = kingBaseTest.getConnect();
        kingBaseTest.createTable();
        kingBaseTest.insert("张三");
        kingBaseTest.query();
        connect.close();
    }

    public static final String url = "jdbc:kingbase8://10.194.188.76:14321/TEST";  //改为自己数据库地址和名字
    public static final String name = "com.kingbase8.Driver";
    public static final String user = "SYSTEM";
    public static final String password = "123456";  //这里的用户名和密码设置为自己的
    public Connection conn = null;

    public Connection getConnect() throws ClassNotFoundException, SQLException {
        Class.forName(name);//指南中的这个方法运行不成功
        conn = DriverManager.getConnection(url, user, password);//获取连接
        return conn;
    }

    public void createTable() throws SQLException {
        Statement statement = conn.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS KING_BASE_TEST(\n" +
                "    \"ID\" SERIAL,\n" +
                "    \"NAME\" VARCHAR(255) NULL,\n" +
                "    PRIMARY KEY(\"ID\")\n" +
                ");");
        statement.close();
    }

    public void insert(String name) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO KING_BASE_TEST (\"NAME\") VALUES(?);");
        preparedStatement.setString(1, name);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void query() throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM KING_BASE_TEST;");
        while (resultSet.next()) {
            System.out.println(resultSet.getInt(1) + ":" + resultSet.getString(2));
        }
        resultSet.close();
        statement.close();
    }
}

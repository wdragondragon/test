package org.example.javabase.baseTest.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 13:07
 * @Description:
 */
public class JdbcTest {
    static Connection connection = Conn.getConnection();
    public static void main(String[] args) throws SQLException {
        testPtmtStr();
    }

    public static void testPtmtStr() throws SQLException {

        PreparedStatement ptmt = Conn.getPtmt(connection,"select * from test_user where username=?");
        ptmt.setString(1,"张三'or'1'='1");
        System.out.println(ptmt.toString());

        PreparedStatement ptmt2 = Conn.getPtmt(connection,"select * from test_user where id=?");
        ptmt2.setInt(1,1);
        System.out.println(ptmt2.toString());


    }
}

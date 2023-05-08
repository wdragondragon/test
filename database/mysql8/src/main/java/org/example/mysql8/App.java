package org.example.mysql8;

import cn.hutool.Hutool;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.NumberUtil;
import com.mysql.cj.util.StringUtils;
import sun.misc.FloatingDecimal;

import java.math.BigDecimal;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;

/**
 * @Author JDragon
 * @Date 2022/10/31 10:15
 * @description:
 */
public class App {
    public static void main(String[] args) throws Exception {
        String url = System.getProperty("url");
        Class.forName("com.mysql.cj.jdbc.Driver");
        if (url == null) {
            url = "jdbc:mysql://192.168.1.150:3306/datax_test";
        }
        String usrname = System.getProperty("username", "root");
        String passWord = System.getProperty("password", "951753");
        String sql = System.getProperty("sql", "select * from t_float4");
        System.out.println("url: " + url);
        System.out.println("username: " + usrname);
        System.out.println("password: " + passWord);
        System.out.println("sql: " + sql);
        Connection con = DriverManager.getConnection(url, usrname, passWord);
        System.out.println("数据库连接成功。");

        Statement statement = con.createStatement();
//        ResultSet resultSet = statement.executeQuery("select * from t_float4_20230410");
//        ResultSetMetaData metaData = resultSet.getMetaData();
//        int columnCount = metaData.getColumnCount();
//        for (int i = 1; i <= columnCount; i++) {
//            System.out.print(metaData.getColumnLabel(i));
////            System.out.print("  "+rsmd.getCatalogName(i));
//            System.out.print("  " + metaData.getColumnClassName(i));
//            System.out.print("  " + metaData.getColumnDisplaySize(i));
//            System.out.print("  " + metaData.getScale(i));
//            System.out.print("  " + metaData.getPrecision(i));
//            System.out.println("  " + metaData.getColumnTypeName(i));
//        }
//        while (resultSet.next()) {
//            for (int i = 1; i <= columnCount; i++) {
//                Object object = resultSet.getObject(i);
//                if (object instanceof Double) {
//                    String s1 = NumberUtil.toStr((Double) object);
//                    System.out.println("double str: " + s1);
//                    String s = new BigDecimal((Double) object).toString();
//                    System.out.println("double: " + s);
//                }
//                if (object instanceof Float) {
//                    String s1 = NumberUtil.toStr((Float) object);
//                    System.out.println("float str: " + s1);
//                    // 结果未做任何处理
//                    String s = new BigDecimal((Float) object).toString();
//                    System.out.println("float: " + s);
//                }
//                System.out.println(resultSet.getObject(i));
//            }
//        }
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        System.out.println(String.format("%30s,%30s,%30s,%30s", "columnName", "columnClassName", "columnTypeName", "columnType"));
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            String columnClassName = metaData.getColumnClassName(i);
            String columnTypeName = metaData.getColumnTypeName(i);
            int columnType = metaData.getColumnType(i);
            String format = String.format("%30s,%30s,%30s,%30s", columnName, columnClassName, columnTypeName, columnType);
            System.out.println(format);
        }
        final DecimalFormat DOUBLE_VALUE_FORMAT = new DecimalFormat("#.###############");

        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                int columnType = metaData.getColumnType(i);
                if (columnType == 7) {
                    System.out.print(resultSet.getFloat(i));
                    System.out.print(" " + resultSet.getString(i));
                    byte[] bytes = resultSet.getBytes(i);
                    System.out.print(" " + Arrays.toString(bytes));
                    int length = bytes == null ? 0 : bytes.length;
                    String s = StringUtils.toAsciiString(bytes, 0, length);
                    if (!resultSet.getString(i).contains("E")) {
                        System.out.print(" " + s);
                    } else {
                        Object object = resultSet.getObject(i);
                        String format = DOUBLE_VALUE_FORMAT.format(object);
                        System.out.print(" " + format);
                    }
                } else {
                    System.out.print(resultSet.getObject(i));
                }

                System.out.print(" ");
            }
            System.out.println();
        }
    }
}

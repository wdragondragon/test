package org.example.gauss.dws;

import com.huawei.gauss200.jdbc.copy.CopyManager;
import com.huawei.gauss200.jdbc.core.BaseConnection;

import java.io.StringReader;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JDragon
 * @date 2023/6/26 9:54
 * @description
 */
public class DwsTest {

    /**
     * 特殊字符映射
     */
    public static final Map<Character, String> SPECIAL_CHARSETS_MAP = new HashMap<>(8, 1f);

    static {
        SPECIAL_CHARSETS_MAP.put('\t', "\\t");
        SPECIAL_CHARSETS_MAP.put('\n', "\\n");
        SPECIAL_CHARSETS_MAP.put('\r', "\\r");
        SPECIAL_CHARSETS_MAP.put('\\', "\\\\");
    }

    public static void main(String[] args) {
        String url = new String("jdbc:gaussdb://10.194.186.223:11432/postgres"); //数据库URL
        String user = new String("gaussdb");            //DWS用户名
        String pass = new String("Gauss@123");             //DWS密码
        String tablename = new String("migration_table"); //定义表信息
        String delimiter = new String("\u0001");              //定义分隔符
        String encoding = new String("UTF8");            //定义字符集
        String driver = "com.huawei.gauss200.jdbc.Driver";
        StringBuffer buffer = new StringBuffer();       //定义存放格式   化数据的缓存

        try {
            //获取源数据库查询结果集
            ResultSet rs = getDataSet();

            //遍历结果集，逐行获取记录
            //将每条记录中各字段值，按指定分隔符分割，由换行符结束，拼成一个字符串
            //把拼成的字符串，添加到缓存buffer
            while (rs.next()) {
                buffer.append(handleSpecialCharsets(rs.getString(1)) + delimiter
                        + handleSpecialCharsets(rs.getString(2)) + delimiter
                        + handleSpecialCharsets(rs.getString(3)) + delimiter
                        + handleSpecialCharsets(rs.getString(4))
                        + "\n");
            }
            rs.close();

            try {
                //建立目标数据库连接
                Class.forName(driver);
                Connection conn = DriverManager.getConnection(url, user, pass);
                BaseConnection baseConn = (BaseConnection) conn;
                baseConn.setAutoCommit(false);

                //初始化表信息
                String sql = "Copy " + tablename + "(id,str_1,str_3,str_2) from STDIN DELIMITER " + "'" + delimiter + "'" + " ENCODING " + "'" + encoding + "'";

                //提交缓存buffer中的数据
                CopyManager cp = new CopyManager(baseConn);
                StringReader reader = new StringReader(buffer.toString());
                cp.copyIn(sql, reader);
                baseConn.commit();
                reader.close();
                baseConn.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace(System.out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //********************************
    // 从源数据库返回查询结果集
    //*********************************
    private static ResultSet getDataSet() {
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.150:3306/datax_test?useSSL=false&allowPublicKeyRetrieval=true", "root", "951753");
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from migration_table");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    private static String handleSpecialCharsets(String value) {
        if (null == value) {
            return "\\N";
        }
        // Add 10% for escaping;
        StringBuilder builder = new StringBuilder(2 + (value.length() + 10) / 10 * 11);
        char[] chars = value.toCharArray();
        for (char achar : chars) {
            String replaceChar = SPECIAL_CHARSETS_MAP.getOrDefault(achar, String.valueOf(achar));
            builder.append(replaceChar);
        }
        return builder.toString();
    }
}

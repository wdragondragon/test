package org.example;

import cn.hutool.core.io.IoUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Hello world!
 * java -jar  -Durl="jdbc:oracle:thin:@//192.168.192.96:1521/aleph2" -Dusername="GPP50" -Dpassword="GPP50" oracle-1.0-SNAPSHOT-jar-with-dependencies.jar
 */
public class OracleTest {
    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String url = System.getProperty("url", "jdbc:oracle:thin:@192.168.1.150:1521:helowin");
            String username = System.getProperty("username", "test");
            String passWord = System.getProperty("password", "test");
            System.out.println(url);
            System.out.println(username);
            System.out.println(passWord);
            Connection con = DriverManager.getConnection(url, username, passWord);
            System.out.println("数据库连接成功。");
//            ResultSet resultSet = con.createStatement().executeQuery("select * from \"t_blob\"");
//            while (resultSet.next()) {
//                System.out.println(resultSet.getInt(1));
//                InputStream binaryStream = resultSet.getBlob("t_clob").getBinaryStream();
//                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                IoUtil.copy(binaryStream, byteArrayOutputStream);
//                System.out.println(byteArrayOutputStream.size());
//            }
//            String msg = "auto_wedata_xn_collect_src_mysql5_1_2_fihiveauto_wedata_xn_collect_src_mysql5" +
//                    "_2_2_fihiveauto_wedata_xn_collect_src_mysql5_3_2_fihiveauto_wedata_xn_collect_src_mysql5_6_2_fihiveauto_wedata_xn_collect_src_mysql5_7" +
//                    "_2_fihiveauto_wedata_xn_collect_src_mysql5_8_2_fihiveauto_wedata_xn_collect_src_mysql5_11_2_fihiveauto_wedata_xn_collect_src_mysql5_12_2_f" +
//                    "ihiveauto_wedata_xn_collect_src_mysql5_13_2_fihiveauto_wedata_xn_collect_src_mysql5_16_2_fihiveauto_wedata_xn_collect_src_mysql5_17_2_fihive" +
//                    "auto_wedata_xn_collect_src_mysql5_18_2_fihiveauto_wedata_xn_collect_src_mysql5_21_2_fihiveauto_wedata_xn_collect_src_mysql5_22_2_fihiveauto_we" +
//                    "data_xn_collect_src_mysql5_23_2_fihiveauto_wedata_xn_collect_src_mysql5_26_2_fihiveauto_wedata_xn_collect_src_mysql5_27_2_fihiveauto_wedata_xn" +
//                    "_collect_src_mysql5_28_2_fihiveauto_wedata_xn_collect_src_mysql5_31_2_fihiveauto_wedata_xn_collect_src_mysql5_32_2_fihiveauto_wedata_xn_collect_" +
//                    "src_mysql5_33_2_fihiveauto_wedata_xn_collect_src_mysql5_36_2_fihiveauto_wedata_xn_collect_src_mysql5_37_2_fihiveauto_wedata_xn_collect_src_mysql5" +
//                    "_38_2_fihiveauto_wedata_xn_collect_src_mysql5_41_2_fihiveauto_wedata_xn_collect_src_mysql5_42_2_fihiveauto_wedata_xn_collect_src_mysqlsrc_mysql5_1" +
//                    "7_2_fihiveauto_wedata_xn_collect_src_mysql5_18_2_fihiveauto_wedata_xn_collect_src_mysql5_21_2_fihiveauto_wedata_xn_collect_src_mysql5_22_2_fihiveauto" +
//                    "_wedata_xn_collect_src_mysql5_23_2_fihiveauto_wedata_xn_collect_src_mysql5_26_2_fihiveauto_wedata_xn_collect_src_mysql5_27_2_fihiveauto_wedata_xn_col" +
//                    "lect_src_mysql5_28_2_fihiveauto_wedata_xn_collect_src_mysql5_31_2_fihiveauto_wedata_xn_collect_src_mysql5_32_2_fihiveauto_wedata_xn_collect_src_mysql5" +
//                    "_33_2_fihiveauto_wedata_xn_collect_src_mysql5_36_2_fihiveauto_wedata_xn_collect_src_mysql5_37_2_fihiveauto_wedata_xn_collect_src_mysql5_38_2_fihiveaut" +
//                    "o_wedata_xn_collect_src_mysql5_41_2_fihiveauto_wedata_xn_collect_src_mysql5_42_2_fihiveauto_wedata_xn_collect_src_mysqsrc_mysql5_17_2_fihiveauto_wedat" +
//                    "a_xn_collect_src_mysql5_18_2_fdddddddddddddd1";
//            msg = msg + msg;
//            System.out.println(msg.getBytes().length);
//            PreparedStatement preparedStatement = con.prepareStatement("insert into \"t_blob\" VALUES(?,?)");
//            preparedStatement.setInt(1, 11);
//            preparedStatement.setBinaryStream(2, IoUtil.toStream((msg).getBytes()));
//            boolean execute = preparedStatement.execute();
//            System.out.println(execute);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

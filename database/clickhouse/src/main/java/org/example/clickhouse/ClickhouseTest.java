package org.example.clickhouse;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.settings.ClickHouseProperties;

import java.sql.*;
import java.util.Properties;

/**
 * @Author JDragon
 * @Date 2022/11/15 11:08
 * @description:
 */
@Slf4j
public class ClickhouseTest {
    public static void main(String[] args) throws Exception {
        String url = System.getProperty("url");
        Class.forName("ru.yandex.clickhouse.ClickHouseDriver");
        ClickHouseProperties clickHouseProperties = new ClickHouseProperties();
        clickHouseProperties.setSocketTimeout(60000);
        if (url == null) {
            url = "jdbc:clickhouse://100.76.160.205:21422/default?ssl=true&sslMode=none";
        }
        String usrname = System.getProperty("username", "u_cmhk_basic_dev");
        String passWord = System.getProperty("password", "Basic@512397458");
        clickHouseProperties.setSsl(true);
        clickHouseProperties.setSslMode("none");
        ClickHouseDataSource clickHouseDataSource =
                new ClickHouseDataSource(url, clickHouseProperties);
        Connection con = clickHouseDataSource.getConnection(usrname, passWord);
        System.out.println("数据库连接成功。");
        String sql = System.getProperty("sql");
        if (sql == null) {
            return;
        }
        Statement statement = con.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnLabel = metaData.getColumnLabel(i + 1);
            System.out.print(columnLabel);
            System.out.print(" ");
        }

        if (resultSet.next()) {
            for (int i = 0; i < columnCount; i++) {
                Object object = resultSet.getObject(i + 1);
                System.out.print(object);
                System.out.print(" ");
            }
            System.out.println();
        }

        resultSet.close();
        statement.close();
        con.close();
//        clickHouseProperties.setSsl(true);
//        clickHouseProperties.setSslMode("none");
//        clickHouseProperties.setSsl(true);
//        clickHouseProperties.setSslMode("none");
//        ClickHouseDataSource clickHouseDataSource =
//                new ClickHouseDataSource(url, clickHouseProperties);
//        ClickHouseConnection connection = clickHouseDataSource.getConnection(usrname, passWord);

    }
}

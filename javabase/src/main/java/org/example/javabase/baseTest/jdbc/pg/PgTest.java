package org.example.javabase.baseTest.jdbc.pg;

import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.20 11:11
 * @Description:
 */
public class PgTest {

    @Test
    public void test(){
        System.out.println(null+",");
    }

    public static PgConn pgConn;
    public static void main(String[] args) throws SQLException {
        pgConn = new PgConn("jdbc:postgresql://10.194.186.227:5432/postgres_db_test","postgres","Mysql@123");
        Connection connection = pgConn.getConnect();
        insert();
        connection.close();
    }

    public static void query() throws SQLException {
        ResultSet resultSet = pgConn.executeQuery("select * from jobs");
        while (resultSet.next()){
            int job_id = resultSet.getInt("job_id");
            System.out.println(job_id);
        }
    }

    public static void insert(){
        pgConn.execute("insert into jobs(job_id,customer_id,description,created_at)" +
                " values('19','222.23','DFAS,FAD,ASFD,D','2020-08-12 00:00:00')");
    }

}

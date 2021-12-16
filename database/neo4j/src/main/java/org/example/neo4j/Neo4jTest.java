package org.example.neo4j;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.sql.SQLException;

/**
 * Hello world!
 */
public class Neo4jTest {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Driver driver = GraphDatabase.driver("bolt://10.194.188.76:7687/", AuthTokens.basic("neo4j", "bmsoft"));
//        SessionConfig ddd = SessionConfig.builder().withDatabase("gra").build();
        Session session = driver.session();
        Result result = session.run("MATCH (n) RETURN n limit 1");
//        while (result.hasNext()) {
//            Record record = result.next();
//            System.out.println(record.get("title").asString() + " " + record.get("name").asString());
//        }
        session.close();
//        Class.forName("org.neo4j.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:neo4j:bolt://10.194.188.76:7687","neo4j","bmsoft");
//
        System.out.println("neo4j连接成功");
    }
}

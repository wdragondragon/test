package org.example.javabase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.03.02 上午 10:55
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test2 {


    public static void main(String[] args) {
        OB ob = new OB(1, "1", "1");
        OB ob5 = new OB(1, "1", "1");
        OB ob2 = new OB(1, "1324", "1");
        OB ob3 = new OB(2, "3", "1");
        OB ob4 = new OB(3, "4", "1");
        List<OB> allMigrationTable = new ArrayList<>();
        allMigrationTable.add(ob);
        allMigrationTable.add(ob2);
        allMigrationTable.add(ob3);
        allMigrationTable.add(ob4);
//        allMigrationTable.add(ob5);
        Map<Integer, OB> collect = allMigrationTable.stream().collect(Collectors.toMap(OB::getId, ob1 -> ob1, (o1, o2) -> o2));
        System.out.println(collect);
    }

    @AllArgsConstructor
    @Getter
    @ToString
    static class OB {
        public int id;

        public String name;

        public String address;


    }
}

package org.example.tableTest;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:31
 * @Description:
 */
public class TableTest {
    @Test
    public void test(){
        List<TestEntity> testEntityList = new ArrayList<>();
        testEntityList.add(new TestEntity("名字一",18,"身份证"));
        testEntityList.add(new TestEntity("名字二",18,"身份证"));
        testEntityList.add(new TestEntity("名字三",18,"身份证"));
        testEntityList.add(new TestEntity("名字四",18,"身份证"));
        testEntityList.add(new TestEntity("名字五",18,"身份证"));
        testEntityList.add(new TestEntity("名字六",18,"身份证"));


        PageTable<TestEntity> testEntityTable =
                TableFactory.buildPageTable(testEntityList.size(),1,10,1,new TableRef<TestEntity>(testEntityList){});

        System.out.println(testEntityTable);


        PageTable<TestEntity> testEntityPageTable = TableFactory.buildPageTable(testEntityList.size(), 1, 10, 1, testEntityList);

        System.out.println(testEntityPageTable);
    }
}

package org.example.javabase.freemaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.13 17:09
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
//        Map<String,Object> root = new HashMap<>();
//        root.put("username","admin");
        FreeMakerUtil freeMakerUtil = new FreeMakerUtil("/template/freemaker/","ftl");
//        freeMakerUtil.fprint("test.ftl",root,"test.html");

        List<User> users = new ArrayList<>();
        users.add(new User("张三"));
        users.add(new User("李四"));
        users.add(new User("黄五"));
        Map<String,Object> root1 = new HashMap<>();
        root1.put("users",users);

        Test1 test1 = new Test1();
//        test1.setPre(true);
        root1.put("test",test1);
        freeMakerUtil.print("test1",root1);
    }
    @Data
    public static class Test1{
        private Test2 test2 = new Test2(true);

        private int number = 1;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Test2{
        private Boolean pre;
    }
}

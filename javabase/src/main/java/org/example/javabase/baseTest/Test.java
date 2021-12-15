package org.example.javabase.baseTest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.14 21:47
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        int[] as = {1,2,3,54};
        for (int i = 0; i < as.length; i++) {
            System.out.println(as[i]);
        }

        for (int a : as) {
            System.out.println(a);
        }

        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        for (String s : stringList) {
            System.out.println(s);
        }


        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("1","one");
        stringMap.put("2","two");

        for (Map.Entry<String, String> stringStringEntry : stringMap.entrySet()) {
            System.out.println(stringStringEntry.getKey()+":"+stringStringEntry.getValue());
        }

        System.out.println(testEnum.枚举一.getInteger());
    }
}

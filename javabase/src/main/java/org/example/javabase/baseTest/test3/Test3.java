package org.example.javabase.baseTest.test3;

import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 00:20
 * @Description:
 */
public class Test3 {
    public static void main(String[] args) {

        String[] strings = new String[]{"1","2","3","4"};

        String[] strings1 = new String[strings.length];

        for (int i = 0; i < strings1.length; i++) {
            strings1[i] = strings[i];
        }

        Class aClass = Class.getClass(40);
        System.out.println(aClass);
        aClass.print();


        List<String> list = new ArrayList<>();

        list.add("1");
        list.add("2");

        System.out.println(list);

        List<String> linkList = new LinkedList<>();

        //key - value
        Map<String,String> map = new HashMap<>();
        map.put("1","str");
        String s = map.get("1");
        System.out.println(s);

        map.remove("1");
        System.out.println(map);

        map.put("2","str2");
        map.put("3","str3");
        map.put("4","str4");
        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            System.out.println("key:"+stringStringEntry.getKey());
            System.out.println("value："+stringStringEntry.getValue());
        }



    }
}

package org.example.javabase.baseTest;

import org.junit.Test;

import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.14 19:42
 * @Description:
 */
public class BaseType {

    @Test
    public void test(){
        List<Integer> integers = Arrays.asList(1, 2);
        Iterator<Integer> iterator = integers.iterator();
        try{
            iterator.next();
            iterator.remove();
        }catch (UnsupportedOperationException e){
            System.out.println("asList可以删个腿");
        }

        ArrayList<Integer> integers1 = new ArrayList<>(integers);
        Iterator<Integer> iterator1 = integers1.iterator();
        iterator1.next();
        iterator1.remove();
        System.out.println(integers1);
    }


    public static void main(String[] args) {
        //八种基本类型
        int num = 0;

        short aShort = 0;

        long aLong = 0L;

        float aFloat = 0f;

        double aDouble = 0;

        boolean aBoolean = true;

        String string = "string";

        char aChar = 'c';

        //对象创建
        BaseTypeEntity baseTypeEntity = new BaseTypeEntity(num,aFloat,aDouble,aBoolean,string,aShort,aLong,aChar);

        //集合类型
        //list
        List<String> stringList = new ArrayList<>();
        stringList.add("1");
        stringList.add("2");
        stringList.add("3");
        stringList.add("3");
        System.out.print("stringList的内容:");
        for (String s : stringList) {
            System.out.print(s);
        }
        System.out.println();

        //set
        Set<String> stringSet = new HashSet<>();
        stringSet.add("1");
        stringSet.add("2");
        stringSet.add("3");
        stringSet.add("3");
        System.out.print("stringSet的内容:");
        for (String s : stringSet) {
            System.out.print(s);
        }
        System.out.println();

        //map
        Map<String,String> stringMap = new HashMap<>();
        stringMap.put("1","one");
        stringMap.put("2","two");
        stringMap.put("3","three");
        stringMap.put("4","four");
        System.out.println("stringMap的内容:");
        for (Map.Entry entry: stringMap.entrySet()) {
            System.out.println("key:"+entry.getKey()+" value:"+entry.getValue());
        }

        //作为方法参数时，引用参数和基本类型的区别
        change(string, baseTypeEntity);
        System.out.println("经过change方法后的string:"+string);
        System.out.println("经过change方法后的baseEntity中的string:"+ baseTypeEntity.string);




    }

    public static void change(String string, BaseTypeEntity baseTypeEntity){
        string = "change";
        baseTypeEntity.setString("change");
    }

    @Test
    public void dataTest(){
        Date date = new Date();
        System.out.println(System.currentTimeMillis()+":"+date.getTime());
    }

}

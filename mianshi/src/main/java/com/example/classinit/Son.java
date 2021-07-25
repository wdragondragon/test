package com.example.classinit;

import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 17:33
 * @Description:
 */
public class Son extends Father{
    private int i = test();

    private static int j = method();

    static {
        System.out.print("(6)");
    }

    {
        System.out.print("(8)");
    }

    public static int method(){
        System.out.print("(10)");
        return 1;
    }

    public int test(){
        System.out.print("(9)");
        return 1;
    }

    Son(){
        System.out.print("(7)");
    }
    public static void main(String[] args) {
        Son s1 = new Son();
        System.out.println();
        Son s2 = new Son();

        Integer [] arr = new Integer[]{};
        Integer a = Arrays.stream(arr).reduce(Integer::sum).orElse(null);
        System.out.println(a);
    }

}

package com.example.classinit;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 17:32
 * @Description:
 */
public class Father {
    private int i = test();

    private static int j = method();

    static {
        System.out.print("(1)");
    }

    {
        System.out.print("(3)");
    }

    Father(){
        System.out.print("(2)");
    }

    public int test(){
        System.out.print("(4)");
        return 1;
    }
    public static int method(){
        System.out.print("(5)");
        return 1;
    }

}

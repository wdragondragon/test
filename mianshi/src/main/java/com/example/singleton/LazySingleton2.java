package com.example.singleton;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 16:11
 * @Description:
 * 静态内部类不会自动随着外部类的加载和初始化而初始化，它是要单独去加载和初始化的
 */
public class LazySingleton2 {
    private LazySingleton2(){

    }
    private static class Inner{
        private static final LazySingleton2 INSTANCE = new LazySingleton2();
    }
    public static LazySingleton2 getInstance(){
        return Inner.INSTANCE;
    }
}

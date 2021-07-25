package com.example.singleton;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 15:49
 * @Description: 饿汉式
 */
public class EagerSingleton {
    public static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton(){

    }
}

package com.example.singleton;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 15:53
 * @Description: 静态代码快
 */
public class StaticSingleton {

    public static final StaticSingleton INSTANCE;

    private String info;

    static {
        Properties pro = new Properties();
        try {
            pro.load(StaticSingleton.class.getClassLoader().getResourceAsStream("single.properties"));
            INSTANCE = new StaticSingleton(pro.getProperty("info"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private StaticSingleton(String info){
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public static void main(String[] args) {
        StaticSingleton staticSingleton = StaticSingleton.INSTANCE;
        System.out.println(staticSingleton.getInfo());
    }
}

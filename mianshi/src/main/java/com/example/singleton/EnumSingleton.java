package com.example.singleton;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 15:50
 * @Description: 枚举
 */
public class EnumSingleton {

    public static EnumSingleton getInstance(){
        return Singleton.INSTANCE.get();
    }

    private enum Singleton{
        INSTANCE;
        private EnumSingleton enumSingleton;

        Singleton(){
            enumSingleton = new EnumSingleton();
        }

        EnumSingleton get(){
            return enumSingleton;
        }
    }

    public static void main(String[] args) {
        EnumSingleton enumSingleton = EnumSingleton.getInstance();
        System.out.println(enumSingleton);
    }
}

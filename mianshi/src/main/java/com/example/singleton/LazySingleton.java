package com.example.singleton;

import java.util.concurrent.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.15 16:00
 * @Description: 懒汉式
 */
public class LazySingleton {

    private volatile static LazySingleton instance;

    private LazySingleton(){

    }
    public static LazySingleton getInstance(){
        if(instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<LazySingleton> c = new Callable<LazySingleton>() {
            @Override
            public LazySingleton call() {
                return LazySingleton.getInstance();
            }
        };
        ExecutorService es = Executors.newFixedThreadPool(2);
        Future<LazySingleton> f1 = es.submit(c);
        Future<LazySingleton> f2 = es.submit(c);

        LazySingleton s1 = f1.get();
        LazySingleton s2 = f2.get();

        System.out.println(s1==s2);
        System.out.println(s1);
        System.out.println(s2);

        es.shutdown();
    }
}

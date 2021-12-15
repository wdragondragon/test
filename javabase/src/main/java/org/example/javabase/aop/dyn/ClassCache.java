package org.example.javabase.aop.dyn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author JDragon
 * @Date 2021.04.24 下午 10:18
 * @Email 1061917196@qq.com
 * @Des:
 */
public class ClassCache {
    private static final Map<String, byte[]> classBytes = new ConcurrentHashMap<>();// 用于存放.class文件的内存

    public static void put(String key, byte[] value) {
        classBytes.put(key, value);
    }

    public static byte[] get(String key) {
        return classBytes.get(key);
    }

    public static boolean contains(String key) {
        return classBytes.containsKey(key);
    }
}

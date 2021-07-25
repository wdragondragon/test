package org.example.aop.dyn;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Author JDragon
 * @Date 2021.04.24 下午 8:35
 * @Email 1061917196@qq.com
 * @Des:
 */

public class MemoryClassLoader extends URLClassLoader {

    public final static MemoryClassLoader INSTANCE = new MemoryClassLoader();

    private final static Map<String, Class<?>> cache = new ConcurrentHashMap<>();

    /**
     * 先根据类名在内存中查找是否已存在该类，若不存在则调用 URLClassLoader的 defineClass方法加载该类
     * URLClassLoader的具体作用就是将class文件加载到jvm虚拟机中去
     */
    private MemoryClassLoader() {
        super(new URL[0], MemoryClassLoader.class.getClassLoader());
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {
        byte[] buf = ClassCache.get(name);
        if (buf == null) {
            return super.findClass(name);
        }
        return defineClass(name, buf, 0, buf.length);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> aClass = cache.get(name);
        if (aClass == null) {
            aClass = super.loadClass(name);
            cache.put(name, aClass);
        }
        return aClass;
    }
}
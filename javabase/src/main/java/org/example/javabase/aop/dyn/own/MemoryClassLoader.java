package org.example.javabase.aop.dyn.own;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 16:52
 * @Description:
 */
public class MemoryClassLoader extends ClassLoader {


    private static final Map<String, MemoryJavaClassObject> CLASS_CACHE_MAP = new HashMap<>(8);

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (!CLASS_CACHE_MAP.containsKey(name)) {
            throw new ClassNotFoundException(name);
        }
        MemoryJavaClassObject classObject = CLASS_CACHE_MAP.get(name);
        byte[] b = classObject.getBytes();

        return super.defineClass(name, b, 0, b.length);
    }

    public void cacheClass(String name, MemoryJavaClassObject object) {
        CLASS_CACHE_MAP.put(name, object);
    }

    public byte[] getClassByte(String name){
        return CLASS_CACHE_MAP.get(name).getBytes();
    }
}

package org.example.dynamicJar.loader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author JDragon
 * @Date 2021.04.25 下午 2:46
 * @Email 1061917196@qq.com
 * @Des:
 */
public class JarLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        JarLoader jarLoader = JarLoader.load("C:\\dev\\IdeaProjects\\study\\test\\out\\artifacts\\javabase_jar\\javabase.jar");
        Class<?> aClass = jarLoader.loadClass("org.example.Test");
        Object instance = aClass.newInstance();
        String[] param = new String[0];
        Object[] params = new Object[]{param};
        Method method = aClass.getDeclaredMethod("main", String[].class);
        method.invoke(instance, params);
    }
}

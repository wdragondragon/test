package org.example.dynamicJar;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Deque;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.24 13:11
 * @Description: 动态加载jar
 */
public class DynamicJarTest {
    public static void main(String[] args) throws ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException,
            InstantiationException,
            InvocationTargetException {
        loadJar("C:\\dev\\IdeaProjects\\study\\test\\out\\artifacts\\javabase_jar\\javabase.jar");
        Class<?> aClass = Class.forName("org.example.Test");
        Object instance = aClass.newInstance();
        String[] param = new String[0];
        Object[] params = new Object[]{param};
        Method method = aClass.getDeclaredMethod("main", String[].class);
        method.invoke(instance, params);
    }

    public static void loadJar(String jarPath) {

        File jarFile = new File(jarPath);
        // 从URLClassLoader类中获取类所在文件夹的方法，jar也可以认为是一个文件夹
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
            return;
        }
        // 获取方法的访问权限以便写回
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            // 获取系统类加载器
            URLClassLoader classLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            URL url = jarFile.toURI().toURL();
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.setAccessible(accessible);
        }
    }
}

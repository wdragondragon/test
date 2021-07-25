package org.example.aop.dyn.own;

import org.example.aop.dyn.own.MemoryClassLoader;
import org.example.aop.dyn.own.MemoryFileManager;
import org.example.aop.dyn.own.MemoryJavaFileObject;

import javax.tools.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @Author JDragon
 * @Date 2021.04.24 下午 8:18
 * @Email 1061917196@qq.com
 * @Des:
 */
public class ClassHelper {


    private final static Boolean SAVE_GENERATED_FILES = Boolean.valueOf(System.getProperty("sun.misc.ProxyGenerator.saveGeneratedFiles"));

    private final static String PACKAGE_NAME = "com.jdragon.proxy";

    private final static String USER_DIR = System.getProperty("user.dir") + "/com/jdragon/proxy/";


    public static Class<?> paramsClass(String className, String sourceCode) {
        String classPath = PACKAGE_NAME + "." + className;
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        try (JavaFileManager manager = new MemoryFileManager(compiler.getStandardFileManager(null, null, null))) {
            List<JavaFileObject> files = Collections.singletonList(new MemoryJavaFileObject(className, sourceCode));
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, files);
            if (!task.call()) {
                throw new Exception("任务调用异常");
            }
            ClassLoader classLoader = manager.getClassLoader(null);
            Class<?> aClass = manager.getClassLoader(null).loadClass(classPath);
            System.out.println("编译完成");
            if (SAVE_GENERATED_FILES) {
                System.out.println("保存文件");
                save(className, classLoader);
                System.out.println("保存文件完成");
            }
            return aClass;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private static void save(String proxyClassName, ClassLoader classLoader) throws IOException {
        byte[] classByte = ((MemoryClassLoader) classLoader).getClassByte(PACKAGE_NAME + "." + proxyClassName);
        File f = new File(USER_DIR, proxyClassName + ".class");
        if (!f.getParentFile().exists()) {
            if (!f.getParentFile().mkdirs()) {
                throw new IOException("创建保存目录失败");
            }
        }
        FileOutputStream fw = new FileOutputStream(f);
        fw.write(classByte);
        fw.close();
    }
}

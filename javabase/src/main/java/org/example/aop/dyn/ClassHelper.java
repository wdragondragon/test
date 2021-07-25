package org.example.aop.dyn;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2021.04.24 下午 9:06
 * @Email 1061917196@qq.com
 * @Des:
 */
public class ClassHelper {

    private static final String SUFFIX = ".java";// 类名后面要跟的后缀

    private static final String REMOVE_SUFFIX_REGEX = "(\\..*)";

    private static final Pattern PACKAGE_REGEX = Pattern.compile("package\\s*(\\S*);");

    private static final Pattern CLASS_REGEX = Pattern.compile("class\\s*(\\S*)\\{");

    public static Class<?> parseClass(String source) {
        String packageName = "";
        Matcher packageMatcher = PACKAGE_REGEX.matcher(source);
        if (packageMatcher.find()) {
            packageName = packageMatcher.group(1) + ".";
        }

        Matcher classMatcher = CLASS_REGEX.matcher(source);
        if (classMatcher.find()) {
            return parseClass(packageName + classMatcher.group(1), source);
        } else {
            throw new RuntimeException("代码中无法获取到类名");
        }
    }

    public static Class<?> parseClass(File file) {
        if (!file.exists()) {
            throw new RuntimeException("文件：" + file.getPath() + "不存在");
        }
        StringBuilder source = new StringBuilder();
        try (FileInputStream is = new FileInputStream(file);
             InputStreamReader streamReader = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(streamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                source.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //去除文件后缀
        String className = file.getName().replaceAll(REMOVE_SUFFIX_REGEX, "");
        return parseClass(className, source.toString());
    }

    public static Class<?> parseClass(String className, String source) {
        if (!ClassCache.contains(className)) {
            compile(className + SUFFIX, source);
        }
        try {
            return MemoryClassLoader.INSTANCE.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过类名和其代码（Java代码字符串），编译得到字节码，返回类名及其对应类的字节码，封装于Map中，值得注意的是，
     * 平常类中就编译出来的字节码只有一个类，但是考虑到内部类的情况， 会出现很多个类名及其字节码，所以用Map封装方便。
     *
     * @param javaName 类名
     * @param javaSrc  Java源码
     */
    public static Map<String, byte[]> compile(String javaName, String javaSrc) {
        // 调用java编译器接口
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler
                .getStandardFileManager(null, null, null);

        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(
                stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(javaName,
                    javaSrc);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager,
                    null, null, null, Collections.singletonList(javaFileObject));
            if (task.call()) {
                return manager.getCurrentLoad();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package org.example.dynamicJar.loader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @Author JDragon
 * @Date 2021.04.25 下午 1:13
 * @Email 1061917196@qq.com
 * @Des:
 */

/**
 * 提供Jar隔离的加载机制，会把传入的路径、及其子路径、以及路径中的jar文件加入到class path。
 */
public class JarLoader extends URLClassLoader {

    private static final Map<String, JarLoader> jarLoaderCache = new ConcurrentHashMap<>();

    private JarLoader(String[] paths) {
        this(paths, JarLoader.class.getClassLoader());
    }

    private JarLoader(String[] paths, ClassLoader parent) {
        super(getURLs(paths), parent);
    }

    public static synchronized JarLoader load(String jarPath) {
        JarLoader jarLoader = jarLoaderCache.get(jarPath);
        if (null == jarLoader) {
            if (StringUtils.isBlank(jarPath)) {
                throw new RuntimeException("jar包地址为空");
            }
            jarLoader = new JarLoader(new String[]{jarPath});
            jarLoaderCache.put(jarPath, jarLoader);
        }

        return jarLoader;
    }

    private static URL[] getURLs(String[] paths) {
        Validate.isTrue(null != paths && 0 != paths.length,
                "jar包路径不能为空.");

        List<String> dirs = new ArrayList<>();
        for (String path : paths) {
            dirs.add(path);
            JarLoader.collectDirs(path, dirs);
        }

        List<URL> urls = new ArrayList<>();
        for (String path : dirs) {
            urls.addAll(doGetURLs(path));
        }

        return urls.toArray(new URL[0]);
    }

    private static void collectDirs(String path, List<String> collector) {
        if (null == path || StringUtils.isBlank(path)) {
            return;
        }

        File current = new File(path);
        if (!current.exists() || !current.isDirectory()) {
            return;
        }

        File[] children = Optional.ofNullable(current.listFiles()).orElse(new File[0]);
        for (File child : children) {
            if (!child.isDirectory()) {
                continue;
            }

            collector.add(child.getAbsolutePath());
            collectDirs(child.getAbsolutePath(), collector);
        }
    }

    private static List<URL> doGetURLs(final String path) {
        Validate.isTrue(!StringUtils.isBlank(path), "jar包路径不能为空.");

        File jarPath = new File(path);

        Validate.isTrue(jarPath.exists(), "jar包路径必须存在.");

        /* set filter */
        FileFilter jarFilter = pathname -> pathname.getName().endsWith(".jar");

        /* iterate all jar */
        File[] allJars = Optional.ofNullable(jarPath.listFiles(jarFilter)).orElse(new File[0]);
        List<URL> jarURLs = new ArrayList<>(allJars.length);

        for (File allJar : allJars) {
            try {
                jarURLs.add(allJar.toURI().toURL());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("系统加载jar包出错", e);
            }
        }

        return jarURLs;
    }
}

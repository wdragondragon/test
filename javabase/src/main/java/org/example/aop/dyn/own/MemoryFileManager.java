package org.example.aop.dyn.own;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.io.IOException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 15:16
 * @Description:
 */
public class MemoryFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final MemoryClassLoader classLoader = new MemoryClassLoader();

    public MemoryFileManager(StandardJavaFileManager manager) {
        super(manager);
    }

    @Override
    public ClassLoader getClassLoader(Location location) {
        return classLoader;
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String name, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        MemoryJavaClassObject object = new MemoryJavaClassObject(name, kind);
        classLoader.cacheClass(name, object);
        return object;
    }

}
package org.example.javabase.aop.dyn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

/**
 * @Author JDragon
 * @Date 2021.04.24 下午 8:35
 * @Email 1061917196@qq.com
 * @Des: 将编译好的.class文件保存到内存当中，这里的内存也就是map映射当中
 */
public final class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    protected final static Map<String, byte[]> currentLoad = new ConcurrentHashMap<>();

    public MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(
            JavaFileManager.Location location, String className,
            JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        if (kind == JavaFileObject.Kind.CLASS) {
            return new ClassOutputBuffer(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind,
                    sibling);
        }
    }

    @Override
    public void close() throws IOException {
        currentLoad.clear();
        super.close();
    }

    public Map<String, byte[]> getCurrentLoad() {
        return currentLoad;
    }

    /**
     * 一个文件对象，用来表示从string中获取到的source，一下类容是按照jkd给出的例子写的
     */
    private static class StringInputBuffer extends SimpleJavaFileObject {
        // The source code of this "file".
        final String code;

        /**
         * Constructs a new JavaSourceFromString.
         *
         * @param name 此文件对象表示的编译单元的name
         * @param code 此文件对象表示的编译单元source的code
         */
        StringInputBuffer(String name, String code) {
            super(toURI(name), Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
            return CharBuffer.wrap(code);
        }

        public Reader openReader() {
            return new StringReader(code);
        }
    }

    /**
     * 将Java字节码存储到classBytes映射中的文件对象
     */
    private static class ClassOutputBuffer extends SimpleJavaFileObject {
        private String name;

        /**
         * @param name className
         */
        ClassOutputBuffer(String name) {
            super(toURI(name), Kind.CLASS);
            this.name = name;
        }

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    ClassCache.put(name, bos.toByteArray());
                    currentLoad.put(name, bos.toByteArray());
                }
            };
        }
    }

    public JavaFileObject makeStringSource(String name, String code) {
        return new StringInputBuffer(name, code);
    }

    static URI toURI(String name) {
        String EXT = ".java";
        File file = new File(name);
        if (file.exists()) {// 如果文件存在，返回他的URI
            return file.toURI();
        }
        try {
            final StringBuilder newUri = new StringBuilder();
            newUri.append("mfm:///");
            newUri.append(name.replace('.', '/'));
            if (name.endsWith(EXT)) {
                newUri.replace(newUri.length() - EXT.length(),
                        newUri.length(), EXT);
            }
            return URI.create(newUri.toString());
        } catch (Exception exp) {
            return URI.create("mfm:///com/sun/script/java/java_source");
        }
    }
}
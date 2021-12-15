package org.example.javabase.aop.dyn.own;

import javax.tools.SimpleJavaFileObject;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.nio.CharBuffer;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 15:18
 * @Description:
 */

public class MemoryJavaFileObject extends SimpleJavaFileObject {

    private final String content;

    protected MemoryJavaFileObject(String className, String content) {
        super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.content = content;
    }

    @Override
    public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
        return CharBuffer.wrap(content);
    }

    @SuppressWarnings("unused")
    public Reader openReader() {
        return new StringReader(content);
    }

}
package org.example;

import java.lang.reflect.Method;

public class JavaclassExecuter {
    public static String execute(byte[] classBytes) {
        HackSystem.clearBuffer();
        ClassModifier cm = new ClassModifier(classBytes);
        byte[] modiBytes = cm.modifyUTF8Constant("java/lang/System", "org/example/HackSystem");
        HotSwapClassLoader loader = new HotSwapClassLoader();
        Class<?> clazz = loader.loadByte(modiBytes);
        try {
            Method method = clazz.getMethod("main", String[].class);
            method.invoke(null, new String[]{null});
        } catch (Throwable e) {
            e.printStackTrace(HackSystem.out);
        }
        return HackSystem.getBufferString();
    }
}

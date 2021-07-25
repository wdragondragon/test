package com.example.aop;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.09 22:04
 * @Description:
 */
public class JdbProxyFactory implements InvocationHandler{

    private final static String LN = System.lineSeparator();

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> target){
        return (T)newProxyInstance(target.getClassLoader(),
                new Class[]{target},
                this);
    }

    public Object newProxyInstance(ClassLoader classLoader,
                                   @NotNull Class<?>[] interfaces,
                                   @NotNull InvocationHandler h) {
        try {
            String sourceCode = generateSourceCode(interfaces);

            String path = JdbProxyFactory.class.getResource("").getPath();
            File f = new File(path + "$Proxy0.java");
            FileWriter fw = new FileWriter(f);
            fw.write(sourceCode);
            fw.close();

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
            Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjects(f);

            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();

            Class<?> clazz = classLoader.loadClass("com.example.aop.$Proxy0");
            Constructor<?> constructor = clazz.getDeclaredConstructor(InvocationHandler.class);
            return constructor.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String generateSourceCode(Class<?>[] interfaces) {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.example.aop;").append(LN);
        sb.append("import java.lang.reflect.InvocationHandler;").append(LN);
        sb.append("import java.lang.reflect.Method;").append(LN);

        sb.append("public class $Proxy0 implements ");
        for (Class<?> anInterface : interfaces) {
            sb.append(anInterface.getName()).append(",");
        }
        sb = new StringBuilder(sb.substring(0, sb.length() - 1));

        sb.append("{").append(LN);

        sb.append("private InvocationHandler h;").append(LN);
        sb.append("public $Proxy0(InvocationHandler h){").append(LN);
        sb.append("this.h = h;").append(LN);
        sb.append("}").append(LN);

        sb.append("private static Method m1;").append(LN);

        sb.append("public void saveUser(){").append(LN);
        sb.append("try{").append(LN);
        sb.append("h.invoke(this,m1,null);").append(LN);
        sb.append("} catch(Throwable e) {").append(LN);
        sb.append("e.printStackTrace();").append(LN);
        sb.append("}").append(LN);
        sb.append("}").append(LN);

        sb.append("static {").append(LN);

        sb.append("try{").append(LN);
        sb.append("m1 =  Class.forName(\"com.example.aop.UserService\").getDeclaredMethod(\"saveUser\");");
        sb.append("} catch(NoSuchMethodException e){").append(LN);
        sb.append("e.printStackTrace();").append(LN);
        sb.append("} catch(ClassNotFoundException e){").append(LN);
        sb.append("e.printStackTrace();").append(LN);
        sb.append("}").append(LN);

        sb.append("}").append(LN);

        sb.append("}").append(LN);

        return sb.toString();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("切面");
        return null;
    }
}

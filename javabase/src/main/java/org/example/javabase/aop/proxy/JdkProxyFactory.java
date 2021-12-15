package org.example.javabase.aop.proxy;

import org.example.javabase.aop.dyn.ClassCache;
import org.example.javabase.aop.dyn.ClassHelper;
import org.example.javabase.freemaker.FreeMakerUtil;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.09 22:04
 * @Description:
 */
public class JdkProxyFactory {

    private final static String LN = System.lineSeparator();

    private final static AtomicInteger PROXY_INDEX = new AtomicInteger(0);

    private final static Boolean SAVE_GENERATED_FILES = Boolean.valueOf(System.getProperty("sun.misc.ProxyGenerator.saveGeneratedFiles"));

    private final static String USER_DIR = System.getProperty("user.dir") + "/com/jdragon/proxy/";

    private final static String PACKAGE_NAME = "com.jdragon.proxy";

    public static Object newProxyInstance(ClassLoader classLoader,
                                          @NotNull Class<?>[] interfaces,
                                          @NotNull InvocationHandler h) {
        try {
            if (interfaces.length == 0) {
                throw new Exception("至少要实现一个接口");
            }
            System.out.println("开始");
            String proxyClass = interfaces[0].getSimpleName() + "$Proxy" + PROXY_INDEX.incrementAndGet();
            Class<?> loadClass = loadClassFormMemory(interfaces[0], proxyClass);
            Constructor<?> constructor = loadClass.getDeclaredConstructor(InvocationHandler.class);
            return constructor.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Class<?> loadClassFormMemory(Class<?> interfaces, String proxyClassName) {
        System.out.println("生成源码");
        String sourceCode = generateSourceCode2(interfaces, proxyClassName);
        System.out.println("生成源码完成");
        System.out.println("编译中");
        Class<?> aClass = ClassHelper.parseClass(PACKAGE_NAME + "." + proxyClassName, sourceCode);
        if (SAVE_GENERATED_FILES) {
            System.out.println("保存文件");
            save(proxyClassName);
            System.out.println("保存文件完成");
        }
        return aClass;
    }

    private static void save(String proxyClassName) {
        byte[] classByte = ClassCache.get(PACKAGE_NAME + "." + proxyClassName);
        File f = new File(USER_DIR, proxyClassName + ".class");
        if (!f.getParentFile().exists()) {
            if (!f.getParentFile().mkdirs()) {
                throw new RuntimeException("创建保存目录失败");
            }
        }
        try (FileOutputStream fw = new FileOutputStream(f);) {
            fw.write(classByte);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateSourceCode2(Class<?> interfaces, String proxyClassName) {
        String interfaceName = interfaces.getName();
        List<MethodEntity> methodEntities = new ArrayList<>();
        methodEntities.add(new MethodEntity(Object.class, "toString", null,
                String.class));
        methodEntities.add(new MethodEntity(Object.class, "hashCode", null,
                int.class));
        methodEntities.add(new MethodEntity(Object.class, "equals", Collections.singletonList(Object.class.getName()),
                boolean.class));

        for (Method declaredMethod : interfaces.getDeclaredMethods()) {
            MethodEntity methodEntity = new MethodEntity();
            methodEntity.setClassName(interfaces);
            methodEntity.setMethodName(declaredMethod.getName());
            List<String> params = new ArrayList<>();
            for (Parameter parameter : declaredMethod.getParameters()) {
                String paramTypeName = parameter.getType().getName();
                params.add(paramTypeName);
            }
            methodEntity.setParamList(params);
            methodEntity.setRetType(declaredMethod.getReturnType());
            methodEntity.setTransferType(declaredMethod.getReturnType());
            methodEntities.add(methodEntity);
        }

        Map<String, Object> map = new HashMap<>(8);
        map.put("package", JdkProxyFactory.PACKAGE_NAME);
        map.put("className", proxyClassName);
        map.put("interface", interfaceName);
        map.put("methodList", methodEntities);
        FreeMakerUtil freeMakerUtil = new FreeMakerUtil("/template/freemaker/", "ftl");
        return freeMakerUtil.printString("proxy", map);
    }

    private static String generateSourceCode(Class<?> interfaces, String packageName) {
        return "package " + packageName + ";" + LN +
                "import java.lang.reflect.InvocationHandler;" + LN +
                "import java.lang.reflect.Method;" + LN +
                "import java.lang.reflect.Proxy;" + LN +
                "public class $Proxy0 extends Proxy implements " +
                interfaces.getName() +
                "{" + LN +
                "public $Proxy0(InvocationHandler h){" + LN +
                "super(h);" + LN +
                "}" + LN +
                "public void saveUser(){" + LN +
                "try{" + LN +
                "super.h.invoke(this,m1,null);" + LN +
                "} catch(Throwable e) {" + LN +
                "e.printStackTrace();" + LN +
                "}" + LN +
                "}" + LN +
                "private static Method m1;" + LN +
                "static {" + LN +
                "try{" + LN +
                "m1 = Class.forName(\"" +
                interfaces.getName() +
                "\").getDeclaredMethod(\"saveUser\");" +
                "} catch(NoSuchMethodException e){" + LN +
                "e.printStackTrace();" + LN +
                "} catch(ClassNotFoundException e){" + LN +
                "e.printStackTrace();" + LN +
                "}" + LN +
                "}" + LN +
                "}" + LN;
    }
}

package org.example.aop.dyn;

import lombok.SneakyThrows;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author JDragon
 * @Date 2021.04.24 下午 8:35
 * @Email 1061917196@qq.com
 * @Des:
 */

public class Test {
    public static void main(String[] args) {
        Test.testMemory();
        Test.testFile();
    }

    @SneakyThrows
    public static void testMemory() {
//        String className = "TestClass";
        String javaSrc = "package org.test.zhjl;import java.util.Random;\r\n" + "\r\n"
                + "public class  TestClass{\r\n" + "\r\n"
                + "	public static void main(String[] args) {\r\n"
                + "		TestClass class1 = new TestClass();\r\n"
                + "		class1.sayHello(\"this is main method\");\r\n"
                + "		Random random = new Random();\r\n"
                + "		int a = random.nextInt(1024);\r\n"
                + "		int b = random.nextInt(1024);\r\n"
                + "		System.out.printf(\r\n"
                + "				Thread.currentThread().getName() + \": \" + \"%d + %d = %d\\n\", a,\r\n"
                + "				b, class1.add(a, b));\r\n"
                + "		System.out.println();\r\n" + "	}\r\n" + "\r\n"
                + "	public void sayHello(String msg) {\r\n"
                + "		System.out.printf(\r\n"
                + "				Thread.currentThread().getName() + \": \" + \"Hello %s!\\n\", msg);\r\n"
                + "	}\r\n" + "\r\n" + "	public int add(int a, int b) {\r\n"
                + "		return a + b;\r\n" + "	}\r\n" + "}\r\n" + "";
        Class<?> clazz = ClassHelper.parseClass(javaSrc);
        Object object = clazz.newInstance();

        // 得到sayHello方法
        Method sayHelloMethod = clazz.getMethod("sayHello", String.class);
        sayHelloMethod.invoke(object, "This is the method called by reflect");

        // 得到add方法
        Method addMethod = clazz.getMethod("add", int.class, int.class);
        Object returnValue = addMethod.invoke(object, 1024, 1024);
        System.out.println(Thread.currentThread().getName() + ": "
                + "1024 + 1024 = " + returnValue);

        // 因为在main方法中，调用了add和sayHello方法，所以直接调用main方法就可以执行两个方法
        Method mainMethod = clazz.getDeclaredMethod("main", String[].class);
        mainMethod.invoke(null, (Object) new String[]{});
    }

    @SneakyThrows
    public static void testFile() {
        File file = new File("C:\\dev\\IdeaProjects\\study\\test\\Hello.java");
        Class<?> clazz = ClassHelper.parseClass(file);
        Object object = clazz.newInstance();

        Method method = clazz.getMethod("print");
        method.invoke(object);

        File base = new File("C:\\dev\\IdeaProjects\\study\\test\\BaseDyn.java");
        Class<?> baseClass = ClassHelper.parseClass(base);
        Method encodeHexBase64 = baseClass.getMethod("encodeHexBase64", String.class);
        Object invoke = encodeHexBase64.invoke(null, "1234123412341234");
        System.out.println(invoke);
    }
}

package org.example.javabase;

/**
 * @Author JDragon
 * @Date 2021.08.17 上午 9:19
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test6 {
    public static void main(String[] args) {
        A i = new B();
        boolean b = i.connectionTest();
        System.out.println(b);
        System.out.println(i.getString());
        System.out.println(i.getString2());
    }

    public interface I {
        boolean connectionTest();


        boolean connectDataSource();

        String getString();

        String getString2();
    }

    public static class A implements I {

        private String string = "1";

        @Override
        public boolean connectionTest() {
            return connectDataSource();
        }

        @Override
        public boolean connectDataSource() {
            return false;
        }

        @Override
        public String getString() {
            return string;
        }

        @Override
        public String getString2() {
            return getString();
        }


    }

    public static class B extends A {

        private String string = "2";

        @Override
        public boolean connectDataSource() {
            return true;
        }

        @Override
        public String getString() {
            return string;
        }
    }
}

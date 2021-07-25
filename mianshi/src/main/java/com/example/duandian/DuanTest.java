package com.example.duandian;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.17 12:35
 * @Description:
 */
public class DuanTest {
    public static void main(String[] args) {
        t15();
    }

    private static void t6() {
        String str = "123456a";
        int i = Integer.parseInt(str);
        //java.lang.NumberFormatException: For input string: "123456a"
    }

    public static void t8() {
        class Foo {
            public int i = 3;
        }
        Object o = (Object) new Foo();
        Foo foo = (Foo) o;
        System.out.println(foo.i);
        //输出3
    }

    private static int t9() {
        int a = 10;
        try {
            System.out.println(a / 0);
            a += 10;
        } catch (ArithmeticException e) {
            a += 10;
            return a;
        } finally {
            a += 10;
        }
        return a;
        //20
    }
    private static void t14(){
        String s = "you have an apple";
        s = s.substring(2,6);
        System.out.println(s);
    }

    private static void t15(){
        int a = 3;
        switch (a){
            case 1:
                System.out.println("1");
            case 2:
                System.out.println("2");
            case 3:
                System.out.println(3);
            case 4:
                System.out.println(4);
                break;
            case 5:
                System.out.println(5);
        }
        //3,4
    }

    private static void t18(){
        //grep -n 'content' file   匹配字符串在文件的位置
        //find . -name 'file'   查找文件的位置
    }
    private static void t19(){
        /**
         关于sleep和wait,以下描述错误的是

         Asleep是线程类的方法，wait是object的方法
         Bsleep不释放对象锁，wait放弃对象锁
         Csleep暂停线程，但监控状态依然保持，结束后会自动恢复
         Dwait进入等待锁定池，只有针对此对象发出notify方法获得对象锁进入运行状态
         正确答案：D

         纠错

         首先，sleep()是Thread类中的方法，而wait()则是Object类中的方法。

         sleep()方法导致了程序暂停，但是他的监控状态依然保持着，当指定的时间到了又会自动恢复运行状态。在调用sleep()方法的过程中，线程不会释放对象锁。

         wait()方法会导致线程放弃对象锁，进入等待此对象的等待锁定池，只有针对此对象调用notify()方法后本线程才进入对象锁定池准备获取对象锁进入运行状态。

         注意是准备获取对象锁进入运行状态，而不是立即获得
        **/
    }
    private static void t20(){

    }
}

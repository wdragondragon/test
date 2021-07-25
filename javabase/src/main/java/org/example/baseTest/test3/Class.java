package org.example.baseTest.test3;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 12:19
 * @Description:
 */
public class Class {

    private static Class aClass;

    String className;
    int studentNum;
    Student[] students;

    private Class(int studentNum){
        this.studentNum = studentNum;
        students = new Student[studentNum];
    }

    public static Class getClass(int studentNum){
        aClass = new Class(studentNum);
        return aClass;
    }

    public void print(){
        System.out.println(aClass.toString());
    }

    public String toString() {
        return "[className:"+ className +",studentNum:"+ studentNum +"]";
    }

    static class Student {
        String name;
        int age;
    }
}

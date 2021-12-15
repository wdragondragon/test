package org.example.javabase.baseTest;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.14 19:42
 * @Description:
 */
public class BaseTypeEntity {

    int num;

    float aFloat;

    double aDouble;

    boolean aBoolean;

    String string;

    short aShort;

    long aLong;

    char aChar;
    @Override
    public String toString() {
        return super.toString();
    }

    public BaseTypeEntity(int num, float aFloat, double aDouble, boolean aBoolean, String string, short aShort, long aLong, char aChar){
        this.num = num;
        this.aFloat = aFloat;
        this.aDouble = aDouble;
        this.aBoolean = aBoolean;
        this.string = string;
        this.aShort = aShort;
        this.aLong = aLong;
        this.aChar = aChar;
    }
    public void setString(String string){
        this.string = string;
    }
}

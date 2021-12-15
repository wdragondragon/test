package org.example.javabase.baseTest;


/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.14 21:55
 * @Description:
 */
public enum testEnum {

    枚举一("枚举一",1),
    枚举二("枚举二",2);

    private String string;

    private Integer integer;

    testEnum(String string, Integer integer){
           this.string = string;
           this.integer = integer;
    }

    public String getString(){
        return string;
    }
    public Integer getInteger(){
        return integer;
    }

    public Integer getInteger(String value1){
        testEnum[] values = testEnum.values();
        for (testEnum testEnum : values) {
            if(testEnum.string.equals(value1)){
                return testEnum.integer;
            }
        }
        return null;
    }
}

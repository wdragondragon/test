package org.example.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:05
 * @Description:
 */
public enum Meat implements Food{
    排骨("排骨"),
    鱼头("鱼头"),
    肉片("肉片");

    private String name;

    private Meat(String name){
        this.name = name;
    }

    public static boolean include(String food) {
        for (Meat meat : Meat.values()) {
            if (meat.name.equals(food)){
                return true;
            }
        }
        return false;
    }

    public static Meat getMeat(String food){
        for (Meat meat : Meat.values()) {
            if (meat.name.equals(food)){
                return meat;
            }
        }
        return null;
    }
}

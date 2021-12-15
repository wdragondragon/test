package org.example.javabase.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:04
 * @Description:
 */
public enum Vegetables implements Food{

    菜心("菜心");

    private String name;

    private Vegetables(String name){
        this.name = name;
    }

    public static boolean include(String food) {
        Vegetables[] vegetables = Vegetables.values();
        for (Vegetables vegetable : vegetables) {
            if (vegetable.name.equals(food)){
                return true;
            }
        }
        return false;
    }
    public static Vegetables getVegetable(String food){
        for (Vegetables vegetables : Vegetables.values()) {
            if (vegetables.name.equals(food)){
                return vegetables;
            }
        }
        return null;
    }
}

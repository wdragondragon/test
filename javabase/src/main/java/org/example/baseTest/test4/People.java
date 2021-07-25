package org.example.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:02
 * @Description:
 */
public abstract class People {

    String name;

    public void eat(Food food) {
        System.out.println(name + ":吃" + food);
    }
}

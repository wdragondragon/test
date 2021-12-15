package org.example.javabase.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:14
 * @Description:
 */
public class test4 {
    public static void main(String[] args) {
        String[] food = {"菜心","鱼头","狗屎"};
        XiaoHong xiaoHong = new XiaoHong();
        XiaoMing xiaoMing = new XiaoMing();
        XiaoJin xiaoJin = new XiaoJin();

        xiaoHong.eat(food);
        xiaoMing.eat(food);
        xiaoJin.eat(food);

    }
}

package org.example.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:13
 * @Description:
 */
public class XiaoMing extends People {

    public XiaoMing() {
        name = "小明";
    }

    public void eat(Meat food) {
        super.eat(food);
    }

    public void eat(Vegetables food) {
        super.eat(food);
    }

    public void eat(String[] foods) {
        for (String food : foods) {
            if (Vegetables.include(food)) {
                this.eat(Vegetables.getVegetable(food));
            } else if (Meat.include(food)) {
                this.eat(Meat.getMeat(food));
            } else {
                System.out.println(name + "：不是肉也不是菜，我不吃" + food);
            }
        }
    }
}

package org.example.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:13
 * @Description:
 */
public class XiaoJin extends People {

    public XiaoJin() {
        name = "小金";
    }

    public void eat(Vegetables food) {
        super.eat(food);
    }

    public void eat(String[] foods) {
        for (String food : foods) {
            boolean include = Vegetables.include(food);
            if (include) {
                this.eat(Vegetables.getVegetable(food));
            } else {
                System.out.println(name + "：不是菜，我不吃" + food);
            }
        }
    }
}

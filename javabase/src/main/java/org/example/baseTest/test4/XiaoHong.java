package org.example.baseTest.test4;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.02 15:07
 * @Description:
 */
public class XiaoHong extends People {

    public XiaoHong() {
        name = "小红";
    }

    public void eat(Meat food) {
        super.eat(food);
    }

    public void eat(String[] foods) {
        for (String food : foods) {
            boolean include = Meat.include(food);
            if (include) {
                this.eat(Meat.getMeat(food));
            } else {
                System.out.println(name + "：不是肉，我不吃" + food);
            }
        }
    }
}

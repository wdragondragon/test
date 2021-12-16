package com.jdragon.springboot.homework.service;

import com.jdragon.springboot.homework.entity.Goods;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:21
 * @Description:
 */
public interface GoodsService {

    List<Goods> queryAll();

    Goods addGoods(Goods goods);

    String buyGoods(String goodsName,String cardNo,int num);

    void updateTest();
}

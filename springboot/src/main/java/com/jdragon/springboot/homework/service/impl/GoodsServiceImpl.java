package com.jdragon.springboot.homework.service.impl;

import com.jdragon.springboot.homework.entity.Goods;
import com.jdragon.springboot.homework.entity.ShopCard;
import com.jdragon.springboot.homework.mapper.GoodsMapper;
import com.jdragon.springboot.homework.mapper.ShopCardMapper;
import com.jdragon.springboot.homework.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:26
 * @Description:
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    ShopCardMapper shopCardMapper;

    @Override
    public List<Goods> queryAll() {
        return goodsMapper.queryAll();
    }

    @Override
    public Goods addGoods(Goods goods) {
        Goods oldGoods = goodsMapper.queryByName(goods.getGoodsName());
        if (oldGoods == null) {
            goodsMapper.addGoods(goods);
            oldGoods = goods;
        } else {
            goodsMapper.changeGoodsNum(oldGoods.getGoodsNum());
            oldGoods.setGoodsNum(oldGoods.getGoodsNum() + goods.getGoodsNum());
        }
        return oldGoods;
    }

    @Override
    public String buyGoods(String goodsName, String cardNo, int num) {
        Goods goods = goodsMapper.queryByName(goodsName);

        if (goods == null) {
            return "货品不存在";
        }
        if (goods.getGoodsNum() >= num) {

            ShopCard shopCard = shopCardMapper.queryCard(cardNo);

            if (shopCard == null) {
                return "不存在该购物卡";
            }

            shopCardMapper.changeBalance(goods.getGoodsPrice().negate(), cardNo);

            goodsMapper.changeGoodsNum(goods.getGoodsNum() - num);

            return "购买成功";
        } else {
            return "货品数量不足";
        }
    }

    @Override
    public void updateTest() {
        
    }
}

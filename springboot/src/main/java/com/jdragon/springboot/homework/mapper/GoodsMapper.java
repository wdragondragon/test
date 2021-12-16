package com.jdragon.springboot.homework.mapper;

import com.jdragon.springboot.homework.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:20
 * @Description:
 */

@Mapper
@Repository
public interface GoodsMapper {

    List<Goods> queryAll();

    void addGoods(@Param("goods")Goods goods);

    void changeGoodsNum(@Param("num") Integer num);

    Goods queryByName(@Param("goodsName") String goodsName);
}

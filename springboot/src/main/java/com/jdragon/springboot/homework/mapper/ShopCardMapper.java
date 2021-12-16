package com.jdragon.springboot.homework.mapper;

import com.jdragon.springboot.homework.entity.Goods;
import com.jdragon.springboot.homework.entity.ShopCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:21
 * @Description:
 */
@Mapper
@Repository
public interface ShopCardMapper {
    ShopCard queryCard(@Param("cardNo")String cardNo);

    void changeBalance(@Param("balance") BigDecimal balance,@Param("cardNo")String cardNo);

    void createCard(@Param("shopCard")ShopCard shopCard);
}

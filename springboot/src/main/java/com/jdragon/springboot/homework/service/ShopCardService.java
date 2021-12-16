package com.jdragon.springboot.homework.service;

import com.jdragon.springboot.homework.entity.ShopCard;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:21
 * @Description:
 */
public interface ShopCardService {

    ShopCard changeBalance(BigDecimal balance,String cardNo);

    ShopCard createCard(ShopCard shopCard);
}

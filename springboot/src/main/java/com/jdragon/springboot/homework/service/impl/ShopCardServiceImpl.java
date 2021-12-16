package com.jdragon.springboot.homework.service.impl;

import com.jdragon.springboot.homework.entity.ShopCard;
import com.jdragon.springboot.homework.mapper.ShopCardMapper;
import com.jdragon.springboot.homework.service.ShopCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:22
 * @Description:
 */
@Service
public class ShopCardServiceImpl implements ShopCardService {

    @Autowired
    ShopCardMapper shopCardMapper;

    @Override
    public ShopCard changeBalance(BigDecimal balance,String cardNo) {
        shopCardMapper.changeBalance(balance,cardNo);

        return shopCardMapper.queryCard(cardNo);
    }

    @Override
    public ShopCard createCard(ShopCard shopCard) {

        ShopCard oldCard = shopCardMapper.queryCard(shopCard.getCardNo());

        if(oldCard==null){
            shopCardMapper.createCard(shopCard);
            oldCard = shopCard;
        }
        return oldCard;

    }
}

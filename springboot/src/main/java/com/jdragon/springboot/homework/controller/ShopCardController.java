package com.jdragon.springboot.homework.controller;

import com.jdragon.springboot.homework.entity.ShopCard;
import com.jdragon.springboot.homework.service.ShopCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:19
 * @Description:
 */

@RestController
@RequestMapping("/shopCard")
public class ShopCardController {

    @Autowired
    ShopCardService shopCardService;


    @PostMapping("/add")
    public ShopCard add(@RequestBody ShopCard shopCard){
        return shopCardService.createCard(shopCard);
    }

    @GetMapping("/addBalance")
    public ShopCard addBalance(@RequestParam("balance")BigDecimal balance,
                           @RequestParam("cardNo")String cardNo){
        return shopCardService.changeBalance(balance,cardNo);
    }
}

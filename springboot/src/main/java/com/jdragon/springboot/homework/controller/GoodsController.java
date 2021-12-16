package com.jdragon.springboot.homework.controller;

import com.jdragon.springboot.homework.entity.Goods;
import com.jdragon.springboot.homework.service.GoodsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:19
 * @Description:
 */
@RestController
@RequestMapping("/goods")
@Api(tags = "商品相关")
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    @GetMapping("/all")
    public List<Goods> queryAll(){
        return goodsService.queryAll();
    }

    @PostMapping("/add")
    public Goods addGoods(@RequestBody Goods goods){
        return goodsService.addGoods(goods);
    }

    @GetMapping("/buy")
    public String buyGoods(@RequestParam("goodsName") String goodsName,
                            @RequestParam("cardNo")String cardNo,
                            @RequestParam("num")Integer num){
        return goodsService.buyGoods(goodsName,cardNo,num);
    }

    @GetMapping("/nul")
    public void nul(){

    }
}

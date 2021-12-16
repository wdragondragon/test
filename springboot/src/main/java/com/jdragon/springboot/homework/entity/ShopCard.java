package com.jdragon.springboot.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.18 16:10
 * @Description:
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopCard {

    private Integer cardId;

    private String holderName;

    private String cardNo;

    private BigDecimal cardBalance;
}

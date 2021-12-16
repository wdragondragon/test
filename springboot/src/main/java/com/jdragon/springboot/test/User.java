package com.jdragon.springboot.test;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author JDragon
 * @Date 2021.05.21 下午 11:46
 * @Email 1061917196@qq.com
 * @Des:
 */
@Data
@AllArgsConstructor
public class User {

    private int id;

    private String username;

    private String password;

    private int age;

    private String phone;
}

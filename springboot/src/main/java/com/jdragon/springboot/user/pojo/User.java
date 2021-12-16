package com.jdragon.springboot.user.pojo;

import lombok.Data;

import lombok.Data;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 22:21
 * @Description:
 */
@Data
public class User {

    private String username;

    private int age;

    private String sb;

    public User(String username) {
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}

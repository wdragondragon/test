package com.jdragon.springboot.user.service;

import com.jdragon.springboot.user.pojo.User;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 22:21
 * @Description:
 */
public interface UserService {
    User queryUser(String name);
}

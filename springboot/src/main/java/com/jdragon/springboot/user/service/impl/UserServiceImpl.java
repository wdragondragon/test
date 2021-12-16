package com.jdragon.springboot.user.service.impl;

import com.jdragon.springboot.user.mapper.UserMapper;
import com.jdragon.springboot.user.pojo.User;
import com.jdragon.springboot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 22:22
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User queryUser(String name) {
        User user = userMapper.queryUser(name);
//        if("王五".equals(user.getName())){
//            user.setName("张三老婆");
//        }
        return user;
    }
}

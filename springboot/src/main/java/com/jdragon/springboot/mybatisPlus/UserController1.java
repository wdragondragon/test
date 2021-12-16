package com.jdragon.springboot.mybatisPlus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.08 15:22
 * @Description:
 */
@RestController
@RequestMapping("mybatisplus")
public class UserController1 {

    @Autowired
    UserMapper1 userMapper1;

    @GetMapping("getInfo")
    public User test(){
//        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username","张三");
//        User user = userMapper1.selectOne(queryWrapper);
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,"张三");
        User user1 = userMapper1.selectOne(lambdaQueryWrapper);
        return user1;
    }

    @GetMapping("/insert")
    public boolean test2(){
        User user = new User();
        user.setUsername("李四");
        user.setAge(22);
        return user.insert();
    }

    @GetMapping("/update")
    public boolean test3(){
        User user = userMapper1.selectById(2);
        user.setAge(30);

        return user.updateById();
    }
}

package com.jdragon.springboot.user.controller;

import com.jdragon.springboot.user.mapper.UserMapper2;
import com.jdragon.springboot.user.pojo.User;
import com.jdragon.springboot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 22:20
 * @Description:
 */

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper2 userMapper2;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/info")
    public User info(@RequestParam("name") String name){
        return userService.queryUser(name);
    }

    @GetMapping("/query/{name}")
    public User queryPath(@PathVariable String name){
        return userService.queryUser(name);
    }


    //真正的连接到数据库去拿数据
    @GetMapping("/all")
    public List<User> all(){
        return userMapper2.selectUserList();
    }

    @GetMapping("/query")
    public User query(@RequestParam String name){
        return userMapper2.selectUserByName(name);
    }
}
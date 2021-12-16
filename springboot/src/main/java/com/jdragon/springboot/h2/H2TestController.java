package com.jdragon.springboot.h2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.25 15:58
 * @Description:
 */
@RestController
@RequestMapping("h2")
public class H2TestController {

    @Autowired
    private H2UserMapper h2UserMapper;

    @GetMapping("userList")
    public List<User> userList(){
        return h2UserMapper.selectUserList();
    }
}

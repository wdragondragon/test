package com.jdragon.springboot.user.mapper;

import com.jdragon.springboot.user.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 22:21
 * @Description:
 */
@Repository
public class UserMapper {

    List<User> users = new ArrayList<User>(){
        {
            add(new User("张三"));
            add(new User("李四"));
            add(new User("王五"));
        }
    };

    public User queryUser(String username){
        System.out.println(username);
        Optional<User> user = users.stream()
                .filter(e -> e.getUsername().equals(username))
                .findFirst();

        return user.orElse(null);
    }
}

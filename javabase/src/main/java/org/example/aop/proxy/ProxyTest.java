package org.example.aop.proxy;

import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.15 22:57
 * @Description:
 */
public class ProxyTest {
    public static void main(String[] args) {
        UserService userServiceImp = new ProxyHandler().bindObject(new UserServiceImpl());
        userServiceImp.saveUser(new User());
        userServiceImp.getUser();
        userServiceImp.isExist("zhjl");

        UserService userService = new ProxyHandler().bindInterface(UserService.class);
        userService.saveUser(new User());
        userService.getUser();
        userService.isExist("zhjl");
    }
}

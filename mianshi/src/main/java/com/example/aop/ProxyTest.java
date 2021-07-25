package com.example.aop;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.15 22:57
 * @Description:
 */
public class ProxyTest {
    public static void main(String[] args) {
        UserService userService = new JdbProxyFactory().get(UserService.class);
        userService.saveUser();
    }
}

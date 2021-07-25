package org.example.aop.proxy;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 18:24
 * @Description:
 */
public class UserServiceImpl implements UserService{
    @Override
    public void saveUser(User user) {
        System.out.println("执行saveUser");
    }

    @Override
    public User getUser() {
        System.out.println("执行getUser");
        return new User();
    }

    @Override
    public Boolean isExist(String username) {
        System.out.println("执行isExist");
        return true;
    }
}

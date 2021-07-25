package org.example.aop.proxy;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.15 22:37
 * @Description:
 */
public interface UserService {
    public void saveUser(User user);

    public User getUser();

    public Boolean isExist(String username);
}

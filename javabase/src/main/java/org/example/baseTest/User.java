package org.example.baseTest;

import java.io.Serializable;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 14:28
 * @Description:
 */
class User implements Serializable {
    public String username;
    public Integer age;
    User(String username){
        this.username = username;
    }
    User(String username,Integer age){
        this.username = username;
        this.age = age;
    }
    public void setUsername(String username){
        this.username = username;
    }
}

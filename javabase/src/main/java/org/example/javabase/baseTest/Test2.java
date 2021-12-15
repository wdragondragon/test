package org.example.javabase.baseTest;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.14 22:02
 * @Description:
 */
public class Test2 {
    public static void change(String username,User user){
        username = "change";
        user.setUsername("change");
    }
    public static void main(String[] args) {
        String username = "username";
        User user = new User("username");
        change(username,user);
        System.out.println(username+":"+user.username);
    }
}

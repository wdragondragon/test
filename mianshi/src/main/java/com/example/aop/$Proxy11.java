package com.example.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.15 22:52
 * @Description:
 */
public class $Proxy11 implements UserService {
    private InvocationHandler h;

    private static Method m1;

    public $Proxy11(InvocationHandler h) {
        this.h = h;
    }


    @Override
    public void saveUser() {
        try {
            this.h.invoke(this, m1, null);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static {
        try {
            m1 = Class.forName("org.example.aop.proxy.UserService").getDeclaredMethod("saveUser");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

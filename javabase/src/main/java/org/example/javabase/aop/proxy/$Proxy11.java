package org.example.javabase.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.15 22:52
 * @Description: jdk动态代理实质
 */
public class $Proxy11 extends Proxy implements UserService {
    private static Method m1;
    private static Method m2;
    private static Method m3;

    public $Proxy11(InvocationHandler h) {
        super(h);
    }


    @Override
    public void saveUser(User user) {
        try {
            super.h.invoke(this, m1, new Object[]{user});
        } catch (RuntimeException | Error e) {
            throw e;
        }catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    @Override
    public User getUser() {
        try {
            return (User) super.h.invoke(this, m2, null);
        } catch (RuntimeException | Error e) {
            throw e;
        }catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    @Override
    public Boolean isExist(String username) {
        try {
            return (Boolean) super.h.invoke(this, m3, new Object[]{username});
        } catch (RuntimeException | Error e) {
            throw e;
        }catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    static {
        try {
            m1 = Class.forName("org.example.javabase.aop.proxy.UserService").getDeclaredMethod("saveUser");
            m2 = Class.forName("org.example.javabase.aop.proxy.UserService").getDeclaredMethod("getUser");
            m3 = Class.forName("org.example.javabase.aop.proxy.UserService").getDeclaredMethod("isExist");
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

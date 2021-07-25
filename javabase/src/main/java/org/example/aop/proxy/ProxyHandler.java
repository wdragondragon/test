package org.example.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 18:08
 * @Description:
 */
public class ProxyHandler implements InvocationHandler {
    private Object object;

    @SuppressWarnings("unchecked")
    public <T> T bindObject(T object) {
        this.object = object;
        return (T) JdkProxyFactory.newProxyInstance(
                this.object.getClass().getClassLoader(),
                this.object.getClass().getInterfaces(),
                this);
    }

    @SuppressWarnings("unchecked")
    public <T> T bindInterface(Class<T> proxyInterface) {
        object = proxyInterface;
        return (T) JdkProxyFactory.newProxyInstance(
                proxyInterface.getClassLoader(),
                new Class[]{proxyInterface},
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        try {
            Object result = null;
            System.out.println("执行" + method.getName() + "之前");
            if (!(object instanceof Class)) {
                result = method.invoke(this.object, args);
            } else {
                System.out.println(((Class<?>) object).getName() + "是接口无方法体");
            }
            System.out.println("执行" + method.getName() + "之后");
            System.out.println("========================");
            return result;
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }
}

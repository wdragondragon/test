package org.example.javabase.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk8动态代理
 */
public class DynaProxyServiceA implements InvocationHandler {
    private Object object;
    /**
     *   将目标对象关联到InvocationHandler接口，返回代理对象obj
     *   调用代理对象的方法时，都会自动调用invoke方法
     */

    public Object bind(Object object){
        this.object = object;
        return Proxy.newProxyInstance(
                this.object.getClass().getClassLoader(),
                this.object.getClass().getInterfaces(),
                this);
    }

    @SuppressWarnings("unchecked")
    public <T> T bindInterface(Class<T> proxyInterface){
        object = proxyInterface;
        return (T)Proxy.newProxyInstance(
                proxyInterface.getClassLoader(),
                new Class[]{proxyInterface},
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("log start");
        if(object instanceof Class<?>){
            Class<?> clazz = (Class<?>) object;
            for (Method clazzMethod : clazz.getDeclaredMethods()) {
                System.out.println(clazzMethod.getName());
            }
        }
        try{
            result = method.invoke(this.object,args);
            Class<?> returnType = method.getReturnType();
            System.out.println(returnType);
        }catch (Exception e){
            throw e;
        }
        System.out.println("log end");
        return result;
    }
    public static void main(String [] args) throws Exception {
        IService service = (IService)new DynaProxyServiceA()
                        .bind(new ServiceImplA());
        service.service("zhjl");

//        IService interfaceService = new DynaProxyServiceA().bindInterface(IService.class);
//
//        interfaceService.service("zhjl");
//
//        UserService service1 = new DynaProxyServiceA().bindInterface(UserService.class);
//        service1.sevice("zhjl",new User());
//        System.out.println(service1.is());
    }
}

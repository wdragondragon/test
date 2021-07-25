package org.example.aop.test;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jdragon.common.http.HttpException;
import com.jdragon.common.http.HttpUtils;
import com.jdragon.common.json.JsonUtils;
import com.jdragon.common.response.normal.Result;
import org.example.aop.IService;
import org.example.aop.ServiceImplA;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * jdk8动态代理
 */
public class DynaProxyHttp implements InvocationHandler {
    private Object object;

    /**
     * 将目标对象关联到InvocationHandler接口，返回代理对象obj
     * 调用代理对象的方法时，都会自动调用invoke方法
     */
    public Object bind(Object object) {
        this.object = object;
        return Proxy.newProxyInstance(
                this.object.getClass().getClassLoader(),
                this.object.getClass().getInterfaces(),
                this);
    }

    @SuppressWarnings("unchecked")
    public <T> T bindInterface(Class<T> proxyInterface) {
        object = proxyInterface;
        return (T) Proxy.newProxyInstance(
                proxyInterface.getClassLoader(),
                new Class[]{proxyInterface},
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.println("log start");
        if (object instanceof Class<?>) {
            Class<?> clazz = (Class<?>) object;
            for (Method clazzMethod : clazz.getDeclaredMethods()) {
                System.out.println(clazzMethod.getName());
            }
        }
        try {
//            result = method.invoke(this.object,args);
            String url = "http://localhost:8081/robot/http";
            HashMap<String, String> param = new HashMap<>();
            param.put("param", "123");
            return robotHandle(url, 2, new RobotMsgResult(), param, method.getGenericReturnType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("log end");
        return null;
    }

    public static Object robotHandle(String url, int request, Object robotMsgResult, HashMap<String, String> params, Type type) {
        try {
            HttpUtils httpUtils = HttpUtils.initJson();

            Map<String, String> map;

            if (robotMsgResult == null) return null;

            if (request == 1) {
                httpUtils.setParamMap(params);
                map = httpUtils.get(url);
            } else {
                String s = JsonUtils.object2Str(robotMsgResult);
                map = JSON.parseObject(s, new TypeReference<Map<String, String>>() {
                });
                httpUtils.setParamMap(params);
                httpUtils.setBody(map);
                map = httpUtils.post(url);
            }
            return JSON.parseObject(checkResult(map), type);
        } catch (Exception e) {
            return null;
        }
    }

    public static String checkResult(Map<String, String> result) {
        String statusCode = result.get("statusCode");
        String resultStr = result.get("result");
        if ("200".equals(statusCode)) {
            return resultStr;
        } else {
            throw new HttpException(resultStr);
        }
    }
}

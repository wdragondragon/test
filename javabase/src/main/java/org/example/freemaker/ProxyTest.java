package org.example.freemaker;

import org.example.aop.UserService;
import org.example.aop.proxy.MethodEntity;
import org.example.aop.proxy.TypeReturn;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 10:51
 * @Description:
 */
public class ProxyTest {
    public static void main(String[] args) {
        Class<?> interfaces = UserService.class;
        String interfaceName = interfaces.getName();
        List<MethodEntity> methodEntities = new ArrayList<>();
        methodEntities.add(new MethodEntity(Object.class, "toString", null,
                String.class));
        methodEntities.add(new MethodEntity(Object.class, "hashCode", null,
                int.class));
        methodEntities.add(new MethodEntity(Object.class, "equals", Collections.singletonList(Object.class.getName()),
                boolean.class));

        for (Method declaredMethod : interfaces.getDeclaredMethods()) {
            MethodEntity methodEntity = new MethodEntity();
            methodEntity.setClassName(interfaces);
            methodEntity.setMethodName(declaredMethod.getName());
            List<String> params = new ArrayList<>();
            for (Parameter parameter : declaredMethod.getParameters()) {
                String paramTypeName = parameter.getType().getName();
                params.add(paramTypeName);
            }
            methodEntity.setParamList(params);
            methodEntity.setRetType(declaredMethod.getReturnType());
            methodEntity.setTransferType(declaredMethod.getReturnType());
            methodEntities.add(methodEntity);
        }
        System.out.println(interfaceName);
        System.out.println(methodEntities);

        Map<String, Object> map = new HashMap<>(8);
        map.put("package", "org.example.aop.proxy");
        map.put("className", "$Proxy11");
        map.put("interface", interfaceName);
        map.put("methodList", methodEntities);
        FreeMakerUtil freeMakerUtil = new FreeMakerUtil("/template/freemaker/", "ftl");

        String proxy = freeMakerUtil.printString("proxy", map);
        System.out.println(proxy);


//        MethodEntity methodEntity = new MethodEntity();
//        methodEntity.setMethodName();

    }
}

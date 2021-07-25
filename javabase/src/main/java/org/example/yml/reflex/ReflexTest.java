package org.example.yml.reflex;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.05 18:16
 * @Description:
 */
public class ReflexTest {
    @SneakyThrows
    public void test() {
        Yaml yaml = new Yaml();
//        InputStream inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("yml/reflex.yml");
        InputStream inputStream = new FileInputStream("C:\\Users\\10619\\IdeaProjects\\study\\test\\javabase\\src\\main\\resources\\yml\\reflex.yml");
        JSONObject loadJson = yaml.loadAs(inputStream, JSONObject.class);

        Class<?> clazz = CustomerProperty.class;

        Property annotation = clazz.getAnnotation(Property.class);

        String[] split = annotation.value().split("\\.");
        JSONObject resultJson = loadJson;

        for (String s : split) {
            resultJson = resultJson.getJSONObject(s);
        }

        if (resultJson != null) {
            CustomerProperty customer = JSON.parseObject(resultJson.toJSONString(), (Type) clazz);
            customer.setIsnull("空对象");
            CustomerProperty copy = Bean2Utils.jsonCopy(customer,clazz.getDeclaredField("isnull"));
            System.out.println(customer);
            System.out.println(copy);
            System.out.println(copy == customer);
        }
    }

    public static void main(String[] args) {
        ReflexTest reflexTest = new ReflexTest();
        reflexTest.test();
    }
}

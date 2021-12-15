package org.example.javabase.yml.simple;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.05 10:39
 * @Description:
 */
public class SimpleTest {

    public void test() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("yml/simple.yml");

//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
//        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//        String temp;
//        StringBuilder stringBuilder = new StringBuilder();
//        while ((temp = bufferedReader.readLine()) != null) {
//            stringBuilder.append(temp).append(System.lineSeparator());
//        }
//        String yamlStr = stringBuilder.toString();
//        System.out.println(stringBuilder.toString());
        Map<String, Object> obj = yaml.load(inputStream);
        System.out.println(obj);
        Object contactDetails = obj.get("contactDetails");
        System.out.println(contactDetails);

        Customer customer = JSON.parseObject(JSONObject.toJSONString(obj), Customer.class);
        System.out.println(customer);

    }
}

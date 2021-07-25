package org.example;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.03.05 下午 12:48
 * @Email 1061917196@qq.com
 * @Des:
 */
@Data
public class Test4 {

    private Class<?> className;

    @SneakyThrows
    public static void main(String[] args) {
        String column = "[{\"index\":1,\"name\":\"街镇\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":1,\"name\":\"街镇\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":2,\"name\":\"类型(城市更新-其他)\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":2,\"name\":\"类型(城市更新-其他)\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":3,\"name\":\"地址\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":4,\"name\":\"拆除宗数\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":5,\"name\":\"占地面积（平方米）\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":6,\"name\":\"建筑面积（平方米）\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":7,\"name\":\"拆除时间\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":8,\"name\":\"拆前图片地址\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":9,\"name\":\"拆后图片地址\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":10,\"name\":\"填表时间\",\"parentNode\":\"\",\"type\":\"date\"},{\"index\":10,\"name\":\"填表时间\",\"parentNode\":\"\",\"type\":\"date\"},{\"index\":3,\"name\":\"地址\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":3,\"name\":\"地址\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":8,\"name\":\"拆前图片地址\",\"parentNode\":\"\",\"type\":\"String\"},{\"index\":9,\"name\":\"拆后图片地址\",\"parentNode\":\"\",\"type\":\"String\"}]";
        JSONArray columnJson = JSON.parseArray(column);

        String data = "[{\"byteSize\":3,\"rawData\":\"生物岛\",\"type\":\"STRING\"},{\"byteSize\":3,\"rawData\":\"生物岛\",\"type\":\"STRING\"},{\"byteSize\":2,\"rawData\":\"其他\",\"type\":\"STRING\"},{\"byteSize\":2,\"rawData\":\"其他\",\"type\":\"STRING\"},{\"byteSize\":11,\"rawData\":\"螺旋大道地铁站B出口旁\",\"type\":\"STRING\"},{\"byteSize\":1,\"rawData\":\"1\",\"type\":\"STRING\"},{\"byteSize\":2,\"rawData\":\"40\",\"type\":\"STRING\"},{\"byteSize\":2,\"rawData\":\"40\",\"type\":\"STRING\"},{\"byteSize\":9,\"rawData\":\"2021/1/16\",\"type\":\"STRING\"},{\"byteSize\":10,\"rawData\":\"生物岛1-1.jpg\",\"type\":\"STRING\"},{\"byteSize\":10,\"rawData\":\"生物岛1-2.jpg\",\"type\":\"STRING\"}]";
        JSONArray dataJson = JSON.parseArray(data);

        System.out.println();

        List<Object> params = new LinkedList<>();

        Method test = Test4.class.getMethod("test");

        Object object = test.invoke(new Test4(),params.toArray());

        Class<?> returnType = test.getReturnType();

        returnType.equals(Void.TYPE);

        System.out.println(object);

        List<String> collect = Arrays.stream(Object.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());

        System.out.println(collect);

        Map<String,String> map = new HashMap<>();
        map.put("className","org.example.Test4");
        Test4 test4 = JSONObject.parseObject(JSONObject.toJSONString(map), Test4.class);
        Class aClass = JSONObject.parseObject(JSONObject.toJSONString(map), Class.class);

        System.out.println(test4);
    }

    public int test(){
        return 1;
    }


    static {
        System.out.println("加载内部类触发");
    }

    public static class Job  {
        public Job() {

        }
    }
}

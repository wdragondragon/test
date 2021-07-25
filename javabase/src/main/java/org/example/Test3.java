package org.example;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.03.03 上午 10:53
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test3 {

    public static void main(String[] args) {
        System.out.println(Enum.toArray().toJSONString());
    }

    static enum Enum implements JsonEnum {
        ADD_PREFIX("addPrefix", "添加前缀"),
        ADD_PREFIX2("addPrefix2", "添加前缀2");

        @Getter
        private String operateName;//算子名称

        @Getter
        private String des;

        Enum(String operateName, String des) {
            this.operateName = operateName;
            this.des = des;
        }

        public static JSONArray toArray() {
            JSONArray jsonArray = new JSONArray();
            List<String> collect = Arrays.stream(Enum.values()).map(java.lang.Enum::name)
                    .collect(Collectors.toList());
            for (Enum value : Enum.values()) {
                jsonArray.add(value.toJson(collect));
            }
            return jsonArray;
        }
    }

    static interface JsonEnum {
        @SneakyThrows
        default JSONObject toJson(List<String> ignore) {
            JSONObject jsonObject = new JSONObject();
            Class<?> aClass = this.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                String name = declaredField.getName();
                if (name.startsWith("$") || ignore.contains(name)) {
                    continue;
                }
                jsonObject.put(name, declaredField.get(this));
                declaredField.setAccessible(false);
            }
            return jsonObject;
        }
    }
}

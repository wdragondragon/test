package org.example.ftp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2021.03.16 上午 11:25
 * @Email 1061917196@qq.com
 * @Des: 用来对json的初始化与寄存，对json链式操作的优化
 */
public class Configuration {
    private JSON json;

    private Configuration() {
    }

    public Configuration(File file) throws IOException {
        this(IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8));
    }

    public Configuration(JSON json) {
        this.json = JSONObject.parseObject(JSONObject.toJSONString(json));
    }

    public Configuration(String string) {
        this.json = JSON.parseObject(string, JSON.class);
    }

    public Configuration(Object o) {
        this.json = JSON.parseObject(JSON.toJSONString(o), JSON.class);
    }


    public static Configuration init(File file) throws IOException {
        return new Configuration(file);
    }

    public static Configuration init(JSON json) {
        return new Configuration(json);
    }

    public static Configuration init(String string) {
        return new Configuration(string);
    }

    public static Configuration init(Object o) {
        return new Configuration(o);
    }

    public Configuration getConfig(String path) {
        Object object = get(path);
        return init(object);
    }

    public <T> T get(String path, Class<T> clazz) {
        Object object = get(path);
        return JSON.parseObject(JSON.toJSONString(object), clazz);
    }

    public <T> T get(String path, Type type) {
        Object object = get(path);
        return JSON.parseObject(JSON.toJSONString(object), type);
    }

    public <T> T get(String path, TypeReference<T> type) {
        Object object = get(path);
        return JSON.parseObject(JSON.toJSONString(object), type);
    }

    private Object get(String path) {
        String[] split = path.split("\\.");
        Object object = json;
        Pattern r = Pattern.compile("(.*)\\[(\\d+)]");
        for (String s : split) {
            String key = s;
            Integer index = null;
            Matcher m = r.matcher(s);
            if (m.find()) {
                key = m.group(1);
                index = Integer.parseInt(m.group(2));
            }
            if (object instanceof JSONObject) {
                object = ((JSONObject) object).get(key);
            } else {
                return null;
            }
            if (index != null && object instanceof JSONArray) {
                object = ((JSONArray) object).get(index);
            }
        }
        return object;
    }
}

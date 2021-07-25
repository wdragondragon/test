package org.example.aop.proxy;

import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.12.16 18:35
 * @Description:
 */
public class TypeReturn {
    public static Class<?> returnPrimitive(Class<?> type) {
        if (type == int.class) {
            return Integer.class;
        } else if (type == double.class) {
            return Double.class;
        } else if (type == float.class) {
            return Float.class;
        } else if (type == long.class) {
            return Long.class;
        } else if (type == boolean.class) {
            return Boolean.class;
        } else if (type == byte.class) {
            return Byte.class;
        } else if (type == short.class) {
            return Short.class;
        } else if (type == char.class) {
            return Character.class;
        }
        return type;
    }
}

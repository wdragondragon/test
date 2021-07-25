package org.example.tableTest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.24 09:48
 * @Description:
 */
public class TableUtils {
    public static Type getTypeArgument(Class<? extends TableRef> aClass) {
        Type[] typeArguments = toParameterizedType(aClass).getActualTypeArguments();
        return typeArguments[0];
    }
    public static ParameterizedType toParameterizedType(Type type) {
        if (type instanceof ParameterizedType) {
            return (ParameterizedType) type;
        } else if (type instanceof Class) {
            return toParameterizedType(((Class<?>) type).getGenericSuperclass());
        }
        return null;
    }
}

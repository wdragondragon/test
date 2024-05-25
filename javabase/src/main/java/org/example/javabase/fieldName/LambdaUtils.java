package org.example.javabase.fieldName;

import org.apache.commons.lang3.StringUtils;

public class LambdaUtils {
    public static <T> String getFieldNameByLambda(SFunction<T, ?> func) {
        SerializedLambda lambda = SerializedLambda.resolve(Test7.Model::getId);
        return resolveFieldName(lambda.getImplMethodName());
    }

    private static String resolveFieldName(String getMethodName) {
        if (getMethodName.startsWith("get")) {
            getMethodName = getMethodName.substring(3);
        } else if (getMethodName.startsWith("is")) {
            getMethodName = getMethodName.substring(2);
        }
        // 小写第一个字母
        return firstToLowerCase(getMethodName);
    }

    private static String firstToLowerCase(String param) {
        if (StringUtils.isEmpty(param)) {
            return "";
        }
        return param.substring(0, 1).toLowerCase() + param.substring(1);
    }
}

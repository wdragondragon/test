package org.example.operate.util;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.08 17:38
 * @Description:
 */
public class StringUtil {

    public static String defaultValue(String fieldValue, List<String> paras) {
        return paras.get(0);
    }

    public static String addValue(String fieldValue, List<String> paras) {
        String type = paras.get(0);
        String value = paras.get(1);
        if(type.equals("start")){
            return value+ fieldValue;
        }
        return "";
    }
}

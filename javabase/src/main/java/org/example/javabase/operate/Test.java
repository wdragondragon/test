package org.example.javabase.operate;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.08 17:11
 * @Description:
 */
@Slf4j
public class Test {
    public static void main(String[] args) {
        try {
            String fieldValue = "fieldValue";
            System.out.println(TransformerUtil.operate("add_default_value",fieldValue,Collections.singletonList("default_value")));
            System.out.println(TransformerUtil.operate("test",fieldValue,Arrays.asList("start", "kfk_")));
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}

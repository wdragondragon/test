package org.example.javabase.fieldName;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author JDragon
 * @Date 2021.11.17 下午 10:09
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test7 {


    @Setter
    @Getter
    static class Model {
        private String id;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(LambdaUtils.getFieldNameByLambda(Model::getId));
    }
}

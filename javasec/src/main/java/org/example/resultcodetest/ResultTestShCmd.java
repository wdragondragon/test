package org.example.resultcodetest;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:08
 * @Description:
 */
public class ResultTestShCmd {
    public Result<String> returnString(){
        return Result.success("成功",null);
    }
    @Test
    public void test(){
        System.out.println(returnString());
        System.out.println(Result.success(new ArrayList<>()));
    }
}

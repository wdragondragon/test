package com.example.bit;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.27 17:08
 * @Description:
 */
public class test1 {
    /**
     * @param
     * @author: Jdragon
     * @date: 给定一个集合S(没有重复元素), 输出它所有的子集
     * 输入 1 2 3
     * 输出
     * 1, 2, 12, 3, 13, 23, 123 下午 5:09
     * @return:
     * @Description:
     **/
    public static void main(String[] args) {
        int[] a = new int[]{1, 2, 3};
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            list.add(a[i]);
            StringBuilder str = new StringBuilder(a[i]);
            for (int j = 0; j < a.length - 1; j++) {
                str.append(a[j]);
                list.add(Integer.valueOf(str.toString()));
            }
        }
        System.out.println(list);
    }
}

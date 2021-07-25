package org.example;

import io.searchbox.core.Cat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author JDragon
 * @Date 2021.03.22 下午 2:35
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test5 {
    private static int sum = 0;
    private static int SCORE = 90;

    public static void main(String[] args) {
//        compute(10, 0);
//        System.out.println(sum);
        Test4.Job job = new Test4.Job();
    }

    public static void compute(int num, int scores) {
        if (num <= 0 || scores > SCORE) {
            return;
        }
        if (num == 1) {
            if (scores + 10 >= SCORE) {
                sum++;
                return;
            }
        }
        for (int i = 0; i <= 10; i++) {
            compute(num - 1, scores + i);
        }
    }
}

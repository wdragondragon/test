package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CombinationSum2 {
    public static List<List<Integer>> combinationSum(int[] a, int x) {
        Arrays.sort(a);
        List<List<List<Integer>>> dp = new ArrayList<>();
        for (int i = 1; i <= x; i++) {
            List<List<Integer>> newList = new ArrayList<>();
            for (int j = 0; j < a.length && a[j] <= i; j++) {
                if (a[j] == i) {
                    newList.add(Collections.singletonList(a[j]));
                } else {
                    for (List<Integer> list : dp.get(i - a[j] - 1)) {
                        if (a[j] <= list.get(0)) {
                            List<Integer> newListSub = new ArrayList<>();
                            newListSub.add(a[j]);
                            newListSub.addAll(list);
                            newList.add(newListSub);
                        }
                    }
                }
            }
            dp.add(newList);
        }
        return dp.get(x - 1);
    }

    public static void main(String[] args) {
        int[] a = {2, 3, 5, 6, 8};
        int x = 8;
        List<List<Integer>> result = combinationSum(a, x);
        System.out.println(result);
    }
}

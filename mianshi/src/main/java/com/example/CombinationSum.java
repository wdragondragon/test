package com.example;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
    public static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(candidates, target, 0, new ArrayList<>(), result);
        return result;
    }
    private static void backtrack(int[] candidates, int remain, int start, List<Integer> tempList, List<List<Integer>> result) {
        if (remain > 0) {
            for (int i = start; i < candidates.length; i++) {
                tempList.add(candidates[i]);
                backtrack(candidates, remain - candidates[i], i, tempList, result); // Note: not i + 1 because we can reuse same elements
                tempList.remove(tempList.size() - 1);
            }
        } else if (remain == 0) {
            result.add(new ArrayList<>(tempList));
        }
    }
    public static void main(String[] args) {
        int[] candidates = {2, 3, 6, 7};
        int target = 7;
        List<List<Integer>> result = combinationSum(candidates, target);
        System.out.println(result);
    }
}

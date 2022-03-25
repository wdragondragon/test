package org.example.leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 * <p></p>
 * <p>create time: 2022/2/19 22:35 </p>
 *
 * @author : Jdragon
 */
public class a0969_煎饼排序 {
    public static void main(String[] args) {
        int[] test = {3, 2, 4, 1};
        a0969_煎饼排序 a0969_煎饼排序 = new a0969_煎饼排序();
        List<Integer> integers = a0969_煎饼排序.pancakeSort(test);
        System.out.println(integers);
    }

    public List<Integer> pancakeSort(int[] arr) {
        List<Integer> result = new LinkedList<>();
        for (int i = arr.length; i > 1; i--) {
            int maxIndex = 0;
            for (int j = 0; j < i; j++) {
                if (arr[j] == i) {
                    maxIndex = j;
                }
            }
            if (maxIndex == i - 1) continue;
            rever(arr, maxIndex);
            rever(arr, i - 1);
            result.add(maxIndex + 1);
            result.add(i);
        }
        return result;
    }

    public void rever(int[] arr, int end) {
        for (int i = 0, j = end; i < j; i++, j--) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
}

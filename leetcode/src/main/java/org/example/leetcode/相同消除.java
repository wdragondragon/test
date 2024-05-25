package org.example.leetcode;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class 相同消除 {
    public static void main(String[] args) {
        int[] nums = {2, 4, 1, 1, 2, 2, 2, 2, 3, 3, 4, 5};
        System.out.println("原数组：" + Arrays.toString(nums) + " ,消除后数组：" + Arrays.toString(delSame(nums)));

        for (int i = 0; i < 10; i++) {
            nums = generateRandomArray(10, 0, 9);
            System.out.println("原数组：" + Arrays.toString(nums) + " ,消除后数组：" + Arrays.toString(delSame(nums)));
        }

    }

    public static int[] delSame(int[] nums) {
        // 初始化已被删除的下标，初始化全为false
        boolean[] delSign = new boolean[nums.length];

        // 填充所有数字最先出现的下标，初始化为-1.即未出现过
        int[] num_earliest_index = new int[10];
        Arrays.fill(num_earliest_index, -1);

        int index = 0;
        int sameNum = 0;
        while (index < nums.length) {
            // 如果当前节点被删除，则跳过
            if (delSign[index]) {
                index++;
                continue;
            }
            // 获取当前节点的数字，保存该数字的最早下标，用于大于4个节点的回溯全删
            int num = nums[index];
            if (num_earliest_index[num] == -1) {
                num_earliest_index[num] = index;
            }
            // 找到下一个没有被删除的节点，并对比是否相同
            int nextNoDelIndex = findNextNoDelIndex(delSign, index);
            boolean same = nextNoDelIndex != -1 && nums[index] == nums[nextNoDelIndex];
            if (!same) {
                //不相同时，判断之前判断连续有几个相同，若两个相同或四个以上相同，则执行删除标记
                if (sameNum == 1 || sameNum >= 3) {
                    int resStop = 0;
                    int deleteSize = 1 + sameNum;
                    // 四个以上相同，则将所有该数字全部标记为删除
                    if (sameNum >= 3) {
                        deleteSize = nums.length - num_earliest_index[num];
                        resStop = num_earliest_index[num];
                    }
                    // 执行回溯标记删除逻辑
                    for (int i = index; i >= resStop && deleteSize != 0; i--) {
                        if (!delSign[i] && nums[i] == num) {
                            deleteSize--;
                            delSign[i] = true;
                        }
                    }
                    // 找到下一个未标记删除的下标
                    index = findLastNoDelIndex(delSign, index);
                }
                // 相同数归零
                sameNum = 0;
            } else {
                // 如果还相同，相同数递增
                sameNum++;
            }
            index++;
        }
        // 筛选出所有未被标记删除的节点
        return IntStream.range(0, nums.length)
                .filter(i -> !delSign[i])
                .map(i -> nums[i])
                .toArray();
    }

    public static int findLastNoDelIndex(boolean[] delSign, int index) {
        for (int i = index - 1; i >= 0; i--) {
            if (!delSign[i]) {
                return i - 1;
            }
        }
        return index;
    }

    public static int findNextNoDelIndex(boolean[] delSign, int index) {
        for (int i = index + 1; i < delSign.length; i++) {
            if (!delSign[i]) {
                return i;
            }
        }
        return -1;
    }


    public static int[] generateRandomArray(int k, int min, int max) {
        if (k <= 0) {
            throw new IllegalArgumentException("Array size must be greater than zero");
        }

        int[] array = new int[k];
        Random random = new Random();

        for (int i = 0; i < k; i++) {
            array[i] = random.nextInt(max - min + 1) + min;
        }

        return array;
    }
}

package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/26 20:26 </p>
 *
 * @author : Jdragon
 */
public class a2016_增量元素之间的最大差值 {
    public static void main(String[] args) {
        a2016_增量元素之间的最大差值 a2016_增量元素之间的最大差值 = new a2016_增量元素之间的最大差值();
        int i = a2016_增量元素之间的最大差值.maximumDifference(new int[]{7, 1, 5, 4});
        System.out.println(i);
    }

    public int maximumDifference(int[] nums) {
        int length = nums.length;
        int max = -1;
        int preMin = nums[0];
        for (int i = 1; i < length; i++) {
            if (nums[i] > preMin) {
                max = Math.max(max, nums[i] - preMin);
            } else {
                preMin = nums[i];
            }
        }
        return max;
    }
}

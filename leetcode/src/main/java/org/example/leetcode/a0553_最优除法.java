package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/27 14:52 </p>
 *
 * @author : Jdragon
 */
public class a0553_最优除法 {
    public String optimalDivision(int[] nums) {
        int n = nums.length;
        if (nums.length == 1) {
            return String.valueOf(nums[0]);
        } else if (n == 2) {
            return String.valueOf(nums[0]) + "/" + String.valueOf(nums[1]);
        }
        StringBuffer res = new StringBuffer();
        res.append(nums[0]);
        res.append("/(");
        res.append(nums[1]);
        for (int i = 2; i < n; i++) {
            res.append("/");
            res.append(nums[i]);
        }
        res.append(")");
        return res.toString();
    }
}

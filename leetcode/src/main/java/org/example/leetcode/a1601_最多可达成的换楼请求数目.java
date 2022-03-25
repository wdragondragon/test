package org.example.leetcode;

import java.util.*;

/**
 * <p></p>
 * <p>create time: 2022/2/28 21:20 </p>
 *
 * @author : Jdragon
 */
public class a1601_最多可达成的换楼请求数目 {
    public static void main(String[] args) {
//        System.out.println(true^true);
//        System.out.println(false^false);
        System.out.println(Integer.bitCount(4));
//        a1601_最多可达成的换楼请求数目 a1601_最多可达成的换楼请求数目 = new a1601_最多可达成的换楼请求数目();
//        int i = a1601_最多可达成的换楼请求数目.maximumRequests(4, new int[][]{{3, 0}, {2, 2}, {3, 0}, {0, 1}, {1, 2}, {0, 0}, {3, 2}, {1, 2}});
//        System.out.println(i);
    }

    public int maximumRequests(int n, int[][] requests) {
        int[] delta = new int[n];
        int ans = 0, m = requests.length;
        for (int mask = 0; mask < (1 << m); ++mask) {
            int cnt = Integer.bitCount(mask);
            if (cnt <= ans) {
                continue;
            }
            Arrays.fill(delta, 0);
            for (int i = 0; i < m; ++i) {
                if ((mask & (1 << i)) != 0) {
                    ++delta[requests[i][0]];
                    --delta[requests[i][1]];
                }
            }
            boolean flag = true;
            for (int x : delta) {
                if (x != 0) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ans = cnt;
            }
        }
        return ans;
    }
}

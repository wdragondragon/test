package org.example.leetcode;

import java.util.Arrays;

/**
 * <p></p>
 * <p>create time: 2022/2/24 21:59 </p>
 *
 * @author : Jdragon
 */
public class a1706_球会落何处 {
    public static void main(String[] args) {
        a1706_球会落何处 a1706_球会落何处 = new a1706_球会落何处();
        int[][] grid = {{1, 1, 1, 1, 1, 1}, {-1, -1, -1, -1, -1, -1}, {1, 1, 1, 1, 1, 1}, {-1, -1, -1, -1, -1, -1}};
        int[] ball = a1706_球会落何处.findBall(grid);
        System.out.println(Arrays.toString(ball));
    }

    public int[] findBall(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] flag = new int[n];
        int x, y;
        for (int i = 0; i < n; i++) {
            int other;
            x = i;
            for (y = 0; y < m; y++) {
                int down = grid[y][x];
                if (down == 1 && x < n - 1) {
                    x = x + 1;
                } else if (down == -1 && x != 0) {
                    x = x - 1;
                } else {
                    flag[i] = -1;
                    break;
                }
                other = grid[y][x];
                if (down + other == 0) {
                    flag[i] = -1;
                    break;
                }
            }
            if (y == m) {
                flag[i] = x;
            }
        }
        return flag;
    }

    public int[] findBall2(int[][] grid) {
        int n = grid[0].length;
        int[] ans = new int[n];
        for (int j = 0; j < n; j++) {
            int col = j;  // 球的初始列
            for (int[] row : grid) {
                int dir = row[col];
                col += dir;  // 移动球
                if (col < 0 || col == n || row[col] != dir) {  // 到达侧边或 V 形
                    col = -1;
                    break;
                }
            }
            ans[j] = col;  // col >= 0 为成功到达底部
        }
        return ans;
    }
}

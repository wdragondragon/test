package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/22 21:24 </p>
 *
 * @author : Jdragon
 */
public class a1994_好子集的数目 {

    public static void main(String[] args) {
        a1994_好子集的数目 a1994_好子集的数目 = new a1994_好子集的数目();
        int i = a1994_好子集的数目.numberOfGoodSubsets(new int[]{4, 2, 3, 15});
        System.out.println(i);
    }

    static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29};
    static final int NUM_MAX = 30;
    static final int MOD = 1000000007;

    public int numberOfGoodSubsets(int[] nums) {
        int[] freq = new int[NUM_MAX + 1];
        for (int num : nums) {
            ++freq[num];
        }

        int[] f = new int[1 << PRIMES.length];
        f[0] = 1;
        for (int i = 0; i < freq[1]; ++i) {
            f[0] = f[0] * 2 % MOD;
        }

        for (int i = 2; i <= NUM_MAX; ++i) {
            if (freq[i] == 0) {
                continue;
            }

            // 检查 i 的每个质因数是否均不超过 1 个
            int subset = 0, x = i;
            boolean check = true;
            for (int j = 0; j < PRIMES.length; ++j) {
                int prime = PRIMES[j];
                if (x % (prime * prime) == 0) {
                    check = false;
                    break;
                }
                if (x % prime == 0) {
                    subset |= (1 << j);
                }
            }
            if (!check) {
                continue;
            }

            // 动态规划
            for (int mask = (1 << PRIMES.length) - 1; mask > 0; --mask) {
                if ((mask & subset) == subset) {
                    f[mask] = (int) ((f[mask] + ((long) f[mask ^ subset]) * freq[i]) % MOD);
                }
            }
        }

        int ans = 0;
        for (int mask = 1, maskMax = (1 << PRIMES.length); mask < maskMax; ++mask) {
            ans = (ans + f[mask]) % MOD;
        }

        return ans;
    }
}

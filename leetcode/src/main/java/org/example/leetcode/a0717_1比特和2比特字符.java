package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/20 17:28 </p>
 *
 * @author : Jdragon
 */
public class a0717_1比特和2比特字符 {
    /**
     * https://leetcode-cn.com/problems/1-bit-and-2-bit-characters/solution/1bi-te-yu-2bi-te-zi-fu-by-leetcode-solut-rhrh/
     */
    public static void main(String[] args) {
        int[] bits = {1, 1, 1, 0};
        System.out.println(isOneBitCharacter(bits));

    }


    public static boolean isOneBitCharacter(int[] bits) {
        int n = bits.length - 1;
        int i = 0;
        while (i < n) {
            i += bits[i] + 1;
        }
        return i == n;
    }

    public static boolean isOneBitCharacter2(int[] bits) {
        int n = bits.length, i = n - 2;
        while (i >= 0 && bits[i] == 1) {
            --i;
        }
        return (n - i) % 2 == 0;
    }
}

package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/21 21:24 </p>
 *
 * @author : Jdragon
 */
public class a0838_推多米诺 {

    public static void main(String[] args) {
        String start = ".L.R...LR..LR.";
        a0838_推多米诺 a0838_推多米诺 = new a0838_推多米诺();
        String s = a0838_推多米诺.pushDominoes2(start);
        System.out.println(s);
    }

    /**
     * 往前寻找 LL，RR，LR相对方向，后回溯赋值
     */
    public String pushDominoes(String dominoes) {
        char[] dominoesChar = dominoes.toCharArray();
        int length = dominoesChar.length;
        int n = 0;
        int pre = 0;
        boolean r = false;
        while (n < length) {
            char temp = dominoesChar[n];
            if (temp == 'L') {
                if (r) {
                    for (int left = pre, right = n - 1; left < right; left++, right--) {
                        dominoesChar[left] = 'R';
                        dominoesChar[right] = 'L';
                    }
                } else {
                    for (int left = pre; left < n; left++) {
                        dominoesChar[left] = 'L';
                    }
                }
                pre = n + 1;
                r = false;
            } else if (temp == 'R') {
                pre = n + 1;
                r = true;
            }
            n++;
        }
        if (r) {
            for (int left = pre; left < n; left++) {
                dominoesChar[left] = 'R';
            }
        }
        return new String(dominoesChar);
    }

    public String pushDominoes2(String dominoes) {
        char[] dominoesChar = dominoes.toCharArray();
        int length = dominoesChar.length;
        char preChar = 'L';
        int n = 0;
        while (n < length) {
            int newPre = n;
            while (newPre < length && dominoesChar[newPre] == '.') {
                newPre++;
            }

            char newPreChar = 'R';
            if (newPre < length) {
                newPreChar = dominoesChar[newPre];
            }

            if (newPreChar == preChar) {
                while (n < newPre) {
                    dominoesChar[n++] = newPreChar;
                }
            } else if (preChar == 'R' && newPreChar == 'L') {
                int k = newPre - 1;
                while (n < k) {
                    dominoesChar[n++] = 'R';
                    dominoesChar[k--] = 'L';
                }
            }
            preChar = newPreChar;
            n = newPre + 1;
        }
        return new String(dominoesChar);
    }
}

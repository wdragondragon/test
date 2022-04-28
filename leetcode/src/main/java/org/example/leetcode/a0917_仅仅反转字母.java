package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/23 21:14 </p>
 *
 * @author : Jdragon
 */
public class a0917_仅仅反转字母 {
    public static void main(String[] args) {
        a0917_仅仅反转字母 a0917_仅仅反转字母 = new a0917_仅仅反转字母();
        String s = a0917_仅仅反转字母.reverseOnlyLetters("Test1ng-Leet=code-Q!");
        System.out.println(s);
    }

    public String reverseOnlyLetters(String s) {
        char[] chars = s.toCharArray();
        int i = 0;
        int j = chars.length - 1;
        while (true) {
            while (i < j && !isEng(chars[i])) {
                i++;
            }
            while (i < j && !isEng(chars[j])) {
                j--;
            }
            if (i >= j) {
                break;
            }
            chars[i] = (char) (chars[i] ^ chars[j]);
            chars[j] = (char) (chars[j] ^ chars[i]);
            chars[i] = (char) (chars[i] ^ chars[j]);
            i++;
            j--;
        }
        return new String(chars);
    }

    public boolean isEng(char c) {
        return (c >= 65 && c <= 90) || (c >= 97 && c <= 122);
    }
}

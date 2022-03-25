package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/3/3 21:33 </p>
 *
 * @author : Jdragon
 */
public class a0258_各位相加 {

    public static void main(String[] args) {
        a0258_各位相加 a0258_各位相加 = new a0258_各位相加();
        int i = a0258_各位相加.addDigits(239);
        System.out.println(i);
    }

    public int addDigits(int num) {
        return (num - 1) % 9 + 1;
    }
}

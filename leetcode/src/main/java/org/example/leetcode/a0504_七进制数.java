package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/3/7 20:03 </p>
 *
 * @author : Jdragon
 */
public class a0504_七进制数 {
    public static void main(String[] args) {
        a0504_七进制数 a0504_七进制数 = new a0504_七进制数();
        String s = a0504_七进制数.convertToBase7(-100);
        System.out.println(s);
    }

    public String convertToBase7(int num) {
        if (num == 0) {
            return "0";
        }
        String sign = num < 0 ? "-" : "";

        num = Math.abs(num);

        StringBuilder stringBuffer = new StringBuilder();
        while (num > 0) {
            stringBuffer.append(num % 7);
            num /= 7;
        }
        stringBuffer.append(sign);

        return stringBuffer.reverse().toString();
    }
}

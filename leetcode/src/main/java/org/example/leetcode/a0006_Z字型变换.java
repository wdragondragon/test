package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/3/1 20:32 </p>
 *
 * @author : Jdragon
 */
public class a0006_Z字型变换 {

    public static void main(String[] args) {
        a0006_Z字型变换 a0006_z字型变换 = new a0006_Z字型变换();
        String paypalishiring = a0006_z字型变换.convert("AB", 1);
        System.out.println(paypalishiring);
    }

    public String convert(String s, int numRows) {
        int length = s.length();
        if (length == 1 || numRows >= length || numRows == 1) {
            return s;
        }

        StringBuffer[] buffers = new StringBuffer[numRows];
        for (int i = 0; i < numRows; i++) {
            buffers[i] = new StringBuffer();
        }

        int x = 0;
        int halfR = numRows - 1;
        int allR = 2 * halfR;
        for (int i = 0; i < length; i++) {
            buffers[x].append(s.charAt(i));
            if (i % allR < halfR) {
                x++;
            } else {
                x--;
            }
        }
        StringBuilder result = new StringBuilder();
        for (StringBuffer buffer : buffers) {
            result.append(buffer);
        }
        return result.toString();
    }
}

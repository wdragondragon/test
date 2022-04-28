package org.example.leetcode;

/**
 * <p></p>
 * <p>create time: 2022/2/18 23:24 </p>
 *
 * @author : Jdragon
 */
public class a1791_找出星型图的中心节点 {
    public static void main(String[] args) {
        int maxChoosableInteger = 20;
        System.out.println(Integer.toBinaryString(maxChoosableInteger));
        System.out.println(Integer.toBinaryString(maxChoosableInteger + 1));
        System.out.println(Integer.toBinaryString((1 << (maxChoosableInteger + 1))));
        System.out.println(Integer.toBinaryString(((1 << (maxChoosableInteger + 1)) - 1)));
        System.out.println(Integer.toBinaryString(((1 << (maxChoosableInteger + 1)) - 1)));
        int pick = (1 << (maxChoosableInteger + 1)) - 1;
        System.out.println(1<<20);
        String binaryString = Integer.toBinaryString(pick);
        System.out.println(binaryString);
        System.out.println(20%5);
    }
}

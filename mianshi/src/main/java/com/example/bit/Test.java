package com.example.bit;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.19 20:02
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        //0 1110 14
        //0 0111 7
        //0 0110 6
        System.out.println(14 & 7);//6

        //1 0111 -7原码
        //1 1001 -7补码=反码+1
        //0 0100 4
        //0 0000 0
        System.out.println(-7 & 4);//0
        //0 0101      01 0100
        System.out.println(5 << 2);//20
        System.out.println(5 >> 2);//1
        //10000000 00000000 00000000 00000101 -5原码
        //11111111 11111111 11111111 11111010 -5反码
        //11111111 11111111 11111111 11111011 -5补码 = 反码+1

        //11111111 11111111 11111111 11111110 -5补码右移两位得到新补码
        //11111111 11111111 11111111 11111101  新补码-1 = 反码
        //10000000 00000000 00000000 00000010  反码取反=-2
        System.out.println(-5 >> 2);//-2

        //11111111 11111111 11111111 11101100 -5补码左移两位得到新补码
        //11111111 11111111 11111111 11101011 新补码-1 = 反码
        //10000000 00000000 00000000 00010100 反码取反=-20
        System.out.println(-5 << 2);//-20
    }
}

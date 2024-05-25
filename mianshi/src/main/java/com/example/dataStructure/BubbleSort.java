package com.example.dataStructure;

/**
 * @Author: JDragon
 * @Data:2023/10/17 21:29
 * @Description:
 */
public class BubbleSort {

    public static void main(String[] args) {
        int[] data = {6, 5, 9, 7, 2, 8};

        for (int datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();

        int temp;
        int length = data.length;
        for (int i = length - 1; i > 0; i--) {
            int flag = 0;
            for (int j = 0; j < i; j++) {
                if (data[j] > data[j + 1]) {
                    temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                    flag++;
                }
            }
            if (flag == 0) {
                break;
            }
        }

        for (int datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();
    }
}

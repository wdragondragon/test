package com.example.dataStructure;

/**
 * @Author: JDragon
 * @Data:2023/10/17 21:57
 * @Description:
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] data = new int[]{9, 7, 5, 3, 4, 6};
        for (int datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();
        int temp;
        int length = data.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                if (data[i] > data[j]) {
                    temp = data[i];
                    data[i] = data[j];
                    data[j] = temp;
                }
            }
        }

        for (int datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();
    }
}

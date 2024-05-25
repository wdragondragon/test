package com.example.dataStructure;

/**
 * @Author: JDragon
 * @Data:2023/10/18 1:07
 * @Description:
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] data = new int[]{9, 7, 5, 3, 4, 6};
        int length = data.length;
        int temp;
        for (int i = 1; i < length; i++) {
            int j = i - 1;
            temp = data[i];
            while (j >= 0 && temp < data[j]) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = temp;
        }

        for (int datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();
    }
}
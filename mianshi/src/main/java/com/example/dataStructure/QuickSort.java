package com.example.dataStructure;

/**
 * @Author: JDragon
 * @Data:2023/10/21 14:54
 * @Description:
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] data = {35, 10, 42, 3, 79, 12, 62, 18, 51, 23, 11};
        for (int datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println();
        quick(data, 0, data.length - 1);
        for (int datum : data) {
            System.out.print(datum + " ");
        }
    }

    static void quick(int[] data, int lf, int rg) {
        int lf_idx = lf + 1;
        int rg_idx = rg;
        if (lf >= rg) {
            return;
        }
        do {
            System.out.print("处理过程： ");
            for (int datum : data) {
                System.out.print(datum + " ");
            }
            System.out.println();
            for (; lf_idx < rg_idx; lf_idx++) {
                if (data[lf_idx] > data[lf]) {
                    break;
                }
            }

            for (; rg_idx >= lf_idx; rg_idx--) {
                if (data[rg_idx] < data[lf]) {
                    break;
                }
            }

            if (lf_idx < rg_idx) {
                swap(data, lf_idx, rg_idx);
            }
        } while (lf_idx < rg_idx);

        swap(data, lf, rg_idx);
        quick(data, lf, rg_idx - 1);
        quick(data, rg_idx + 1, rg);
    }

    public static void swap(int[] data, int x, int y) {
        int temp = data[x];
        data[x] = data[y];
        data[y] = temp;
    }
}

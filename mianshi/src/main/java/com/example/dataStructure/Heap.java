package com.example.dataStructure;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.19 14:15
 * @Description: 堆积排序
 */
public class Heap {
    public static void main(String[] args) {
        int i, size, data[] = {0, 5, 6, 4, 8, 3, 2, 7, 1};
        size = 9;
        System.out.print("原始数组：");
        for (i = 1; i < size; i++) {
            System.out.print("[" + data[i] + "]");
        }
        heap(data, size);
        System.out.print("\n排序结果：");
        for (i = 1; i < size; i++) {
            System.out.print("[" + data[i] + "]");
        }
        System.out.println();
    }

    public static void heap(int data[], int size) {
        int i, j, tmp;
        for (i = (size / 2); i > 0; i--) {
            ad_heap(data, i, size - 1);
        }
        System.out.print("\n堆积内容：");
        for (i = 1; i < size; i++) {
            System.out.print("[" + data[i] + "]");
        }
        System.out.println();
        for (i = size - 2; i > 0; i--) {
            tmp = data[i + 1];
            data[i + 1] = data[1];
            data[1] = tmp;
            ad_heap(data, 1, i);
            System.out.print("\n处理过程：");
            for (j = 1; j < size; j++)
                System.out.print("[" + data[j] + "]");
        }
    }

    public static void ad_heap(int data[], int i, int size) {
        int j, tmp, post;
        j = 2 * i;
        tmp = data[i];
        post = 0;
        while (j <= size && post == 0) {
            if (j < size) {
                if (data[j] < data[j + 1]) {
                    j++;
                }
            }
            if (tmp >= data[j])
                post = 1;
            else {
                data[j / 2] = data[j];
                j = 2 * j;
            }
        }
        data[j / 2] = tmp;
    }
}

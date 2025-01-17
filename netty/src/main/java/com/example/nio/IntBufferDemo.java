package com.example.nio;

import java.nio.IntBuffer;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.08 20:59
 * @Description:
 */
public class IntBufferDemo {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(8);

        for (int i = 0; i < buffer.capacity(); ++i) {
            int j = 2 * (i + 1);
            buffer.put(j);
        }

        buffer.flip();
        while (buffer.hasRemaining()) {
            int j = buffer.get();
            System.out.print(j + " ");
        }
    }
}

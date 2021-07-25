package com.example.nio;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.09 21:59
 * @Description:
 */
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);

        for (int i = 0; i < buffer.capacity(); ++i) {
            buffer.put((byte) i);
        }

        ByteBuffer readOnly = buffer.asReadOnlyBuffer();

        for (int i = 0; i < buffer.capacity(); ++i) {
            byte b = buffer.get(i);
            b *= 10;
            buffer.put(i, b);
        }

        readOnly.position(0);
        readOnly.limit(buffer.capacity());

        while (readOnly.remaining() > 0) {
            System.out.println(readOnly.get());
        }
    }
}

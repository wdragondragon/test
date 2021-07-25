package com.example.nio;

import java.nio.ByteBuffer;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.08 22:21
 * @Description:
 */
public class BufferWrap {

    public void myMethod() {
        ByteBuffer buffer1 = ByteBuffer.allocate(10);

        byte[] array = new byte[10];
        ByteBuffer buffer2 = ByteBuffer.wrap(array);
    }
}

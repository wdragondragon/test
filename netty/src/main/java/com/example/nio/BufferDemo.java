package com.example.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.08 21:01
 * @Description:
 */
public class BufferDemo {
    public static void main(String[] args) throws Exception {
        FileInputStream fin = new FileInputStream("D:\\test.txt");
        FileChannel fc = fin.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(10);

        fc.read(buffer);
        output("调用read()", buffer);

        buffer.flip();
        output("调用flip()", buffer);

        while (buffer.remaining() > 0) {
            byte b = buffer.get();
            System.out.print(((char)b));
        }
        System.out.println();

        output("调用get()", buffer);

        buffer.clear();
        output("调用clear()", buffer);

        fin.close();
    }

    public static void output(String step, Buffer buffer) {
        System.out.println(step + ":");
        System.out.print("capacity:" + buffer.capacity() + ",");
        System.out.print("position:" + buffer.position() + ",");
        System.out.println("limit:" + buffer.limit());
        System.out.println();
    }
}

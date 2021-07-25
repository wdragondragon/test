package com.example.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.09 22:15
 * @Description: n内存映射
 */
public class MappedBuffer {
    static private final int start = 0;
    static private final int size = 1024;

    public static void main(String[] args) throws IOException {
        RandomAccessFile raf = new RandomAccessFile("D://test.txt", "rw");
        FileChannel fc = raf.getChannel();

        MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, start, size);
        //与io模型不同，改变了缓存区之后，文件内容也会跟着改变。
        mbb.put(0, (byte) 97);
        mbb.put(1023, (byte) 122);

        raf.close();

    }
}

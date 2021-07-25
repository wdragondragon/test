package com.example.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.09 22:09
 * @Description:
 */
public class DirectBuffer {
    public static void main(String[] args) throws IOException {
        String infile = "D://test.txt";
        FileInputStream fin = new FileInputStream(infile);
        FileChannel fcin = fin.getChannel();

        String outfile = String.format("D://testcopy.txt");
        FileOutputStream fout = new FileOutputStream(outfile);
        FileChannel fcout = fout.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

        while(true){
            buffer.clear();

            int r = fcin.read(buffer);

            if(r==-1){
                break;
            }

            buffer.flip();

            fcout.write(buffer);
        }
    }
}

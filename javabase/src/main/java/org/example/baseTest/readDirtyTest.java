package org.example.baseTest;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.06 12:22
 * @Description:
 */
public class readDirtyTest {
    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\10619\\Desktop\\项目\\北明汇聚\\脏数据\\785-1596592023142.json1596592080009.log");
        FileInputStream fileInputStream = new FileInputStream(file);
        BufferedReader bf = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
        String line = null;
        boolean readSign = false;
        while((line=bf.readLine())!=null){
            if(readSign){
                System.out.println(line);
                readSign = false;
            }else{
                readSign = line.contains("脏数据");
            }
        }
    }
}

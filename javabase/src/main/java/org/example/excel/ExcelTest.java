package org.example.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.16 09:30
 * @Description:
 */
public class ExcelTest {
    public static void main(String[] args) throws IOException {
        String filePath = System.getProperty("user.dir");
        String projectPath = "/javabase/src/main/resources/";
        String fileName = filePath + projectPath + "test.xlsx";
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        List<User> users = ExcelUtils.simpleRead(fileInputStream, User.class);
        for (User user : users) {
            System.out.println(user);
        }

        fileInputStream = new FileInputStream(file);
        List<User> userList = ExcelUtils.readSheet(fileInputStream, "Sheet2", User.class);
        for (User user : userList) {
            System.out.println(user);
        }

//        String fileName2 = filePath + projectPath + "测试模板.xlsx";
//        File file2 = new File(fileName2);
//        FileOutputStream fileOutputStream = new FileOutputStream(file2);
//        ExcelUtils.simpleWrite(fileOutputStream,"sheet",User.class);
    }
}

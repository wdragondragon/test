package org.example.baseTest;

import java.io.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.15 08:54
 * @Description:
 */
public class IOTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        IOTest ioTest = new IOTest();

        String filePath = System.getProperty("user.dir");
        String projectPath = "/javabase/src/main/resources/";
        File file = new File(filePath + projectPath, "test.txt");

        if (!file.exists()) {
            System.out.println("文件不存在");
            return;
        }
        //字符流
        ioTest.read(file);
        //字节流
        ioTest.readInStream(file);

        //序列化
        File objFile = new File(filePath + projectPath,"obj");
        ioTest.objectSerializable(objFile);
    }

    public void readInStream(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader fileReader = new InputStreamReader(fileInputStream);
        BufferedReader reader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        while ((temp = reader.readLine()) != null) {
            stringBuilder.append(temp);
        }
        System.out.println(stringBuilder.toString());
        reader.close();
        fileReader.close();
        fileInputStream.close();
    }

    public void read(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        while ((temp = reader.readLine()) != null) {
            stringBuilder.append(temp);
        }
        System.out.println(stringBuilder.toString());
        reader.close();
        fileReader.close();
    }

    public void objectSerializable(File objFile) throws IOException, ClassNotFoundException {
        User user = new User("张三");
        FileOutputStream fileOutputStream = new FileOutputStream(objFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(user);


        FileInputStream fileInputStream = new FileInputStream(objFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        User user1 = (User)objectInputStream.readObject();

        System.out.println(user1.username);
    }
}

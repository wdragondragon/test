package org.example.javabase.io;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jdragon.common.http.HttpUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.14 18:42
 * @Description:
 */
public class test {
    public static void main(String[] args) throws IOException {
//        List<String> stringList = printIdsStrList("C:\\Users\\10619\\Desktop\\12.txt", "C:\\Users\\10619\\Desktop\\作业列表.txt");
        List<String> stringList = printIdsStrList("C:\\Users\\10619\\Desktop\\1111.txt");
        List<String> failList = new ArrayList<>();
        for (String s : stringList) {
            HttpUtils httpUtils = HttpUtils.init();
            httpUtils.setHeader("Referer", "http://10.197.149.145:31517/converge/work/task");
            httpUtils.setHeader("X-Project-Id", "2eabcc58a28da30c681afded376d06dg");
            httpUtils.setHeader("X-Access-Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InN6Z191c2VyIn0.ASP7INk9-ow1RZpcM21tNdPF8A8uZunrLOqcPaPbyqU");
            httpUtils.setHeader("X-Requested-With", "XMLHttpRequest");
            httpUtils.setHeader("Content-Type", "application/json;charset=utf-8");
            httpUtils.setParam("idsStr", s);
            httpUtils.setConnectTimeout(30000);
            httpUtils.setSocketTimeout(30000);
            Map<String, String> stringMap = httpUtils.get("http://10.197.149.145:31517/api/data-collect/job/startJobs");
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(stringMap));
            JSONObject result = jsonObject.getJSONObject("result");
            Integer code = result.getInteger("code");
            if (code != 200) {
                System.out.println("启动失败:" + s);
                continue;
            }

            JSONObject dataJson = result.getJSONObject("data");
            Map<String, String> dataMap = JSON.toJavaObject(dataJson, Map.class);
            for (Map.Entry<String, String> dataItem : dataMap.entrySet()) {
                String message = dataItem.getKey() + ":" + dataItem.getValue();
                System.out.println(message);
                if(!"作业启动成功".equals(dataItem.getValue())){
                    failList.add(message);
                }
            }
        }

        System.out.println("启动失败作业");
        for (String s : failList) {
            System.out.println(s);
        }
    }

    public static List<String> printIdsStrList(String sourceName, String idsStrFileName) throws IOException {
        List<String> idsStrList = new ArrayList<>();

        File file = new File(sourceName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bf = new BufferedReader(fileReader);
        String str;
        StringBuilder buffer = new StringBuilder();
        int i = 0;
        while ((str = bf.readLine()) != null) {
            buffer.append(str);
            i++;
            if (i == 10) {
                idsStrList.add(buffer.toString());
                buffer.append("\r\n");
                i = 0;
            } else {
                buffer.append(",");
            }
        }
        File file1 = new File(idsStrFileName);
        FileWriter fileWriter = new FileWriter(file1);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(buffer.toString());
        bw.flush();
        bw.close();
        fileWriter.close();

        bf.close();
        fileReader.close();
        return idsStrList;
    }

    public static List<String> printIdsStrList(String idsStrFileName) throws IOException {
        List<String> idsStrList = new ArrayList<>();
        File file = new File(idsStrFileName);
        FileReader fileReader = new FileReader(file);
        BufferedReader bf = new BufferedReader(fileReader);
        String str;
        while ((str = bf.readLine()) != null) {
            idsStrList.add(str);
        }
        return idsStrList;
    }
}

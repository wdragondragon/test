package org.example.javabase;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2021.11.17 下午 10:09
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test7 {
    final String regex = "([^,]*?):(\\d*)";
    final String string = "192.168.1.1:9092,192.168.1.2:9092,192.168.1.2:9092";
    final String subst = "";

    final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
    final Matcher matcher = pattern.matcher(string);

    public static void main(String[] args) throws InterruptedException {
//        String s = FileUtil.readUtf8String(new File("D:\\Desktop\\paas.cmft.com.har.txt"));
//        JSONArray read = (JSONArray)JSONPath.read(s, "$.log.entries[1]._webSocketMessages.data");
//        List<String> strings = new ArrayList<>();
//        for (Object o : read) {
//            String trim = ((String) o).trim();
//            strings.add(trim);
//        }
//        FileUtil.writeLines(strings,"D:\\Desktop\\dump.log", "UTF-8");
        assert true : "打印断言";
        System.out.println("end");
    }

    public void test() {
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }


}

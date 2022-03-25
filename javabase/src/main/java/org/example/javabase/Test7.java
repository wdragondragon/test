package org.example.javabase;

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
        Test7 test7 = new Test7();
        test7.test();
    }

    public void test(){
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }


}

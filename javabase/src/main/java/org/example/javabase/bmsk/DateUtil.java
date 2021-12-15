package org.example.javabase.bmsk;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getNowStr(String str){
        SimpleDateFormat dateFormat = new SimpleDateFormat(str);
        String date = dateFormat.format(new Date());
        return date;
    }
}

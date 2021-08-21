package org.example;


import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Author JDragon
 * @Date 2021.03.22 下午 2:35
 * @Email 1061917196@qq.com
 * @Des:
 */
public class Test5 {
    static String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

    static TimeZone timeZoner = TimeZone.getTimeZone("GMT+8");

    public static void main(String[] args) throws ParseException {

        FastDateFormat dateFormatter = FastDateFormat.getInstance(
                datetimeFormat, timeZoner);

        long time = new Date().getTime();

        Date parse = dateFormatter.parse("2021-08-09 00:00:00");
        System.out.println(parse);

        parse = dateFormatter.parse("2021-08-09 00:00:00");
        System.out.println(parse);


        parse = dateFormatter.parse(String.valueOf(time));
        System.out.println(parse);
    }
}

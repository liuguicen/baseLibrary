package com.lgc.baselibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *      author : liuguicen
 *      time : 2018/12/31
 *      version : 1.0
 * <pre>
 */

public class TimeUtil {
    /**
     * 获取当前时间用年月日时分秒的形式
     */
    public static String curTimeToString() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒", Locale.CHINA);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        return time;
    }
}

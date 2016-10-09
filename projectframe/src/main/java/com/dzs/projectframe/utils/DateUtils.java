package com.dzs.projectframe.utils;

import android.annotation.SuppressLint;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-23 上午9:53:41
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {

    private final static long yearLevelValue = 365 * 24 * 60 * 60 * 1000;
    private final static long monthLevelValue = 30 * 24 * 60 * 60 * 1000;
    private final static long dayLevelValue = 24 * 60 * 60 * 1000;
    private final static long hourLevelValue = 60 * 60 * 1000;
    private final static long minuteLevelValue = 60 * 1000;
    private final static long secondLevelValue = 1000;
    private static final String DATEFORMAT_WITHMINUTS = "yyyy-MM-dd HH:mm:ss";
    private static final String DATEFORMAT = "yyyy-MM-dd";

    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
    private final static String[] constellationArr = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座",
            "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};


    /**
     * 根据月份和日期计算星座
     *
     * @param month 月份
     * @param day   日期
     * @return
     */
    public static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static String getTimeStamp() {
        return String.valueOf(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 获取当前系统时间,以固定格式返回
     *
     * @param format 固定格式
     */
    public static String getCurrentSystemTime(String format) {
        if (StringUtils.isEmpty(format)) throw new IllegalArgumentException();
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取今天零点时间的毫秒数
     *
     * @return long
     */
    public static long getTodayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * 将时间以固定格式输出
     *
     * @param longTime   时间
     * @param dateFormat 日期格式
     * @return String
     */
    public static String formatTime(long longTime, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(longTime);
    }

    /**
     * 将时间以固定格式输出
     *
     * @param date       时间
     * @param dateFormat 日期格式
     * @return String
     */
    public static String formatTime(Date date, String dateFormat) {
        return new SimpleDateFormat(dateFormat).format(date);
    }

    /**
     * 将时间以固定格式输出
     *
     * @param longTime   时间
     * @param dateFormat 日期格式
     * @return String
     */
    public static String formatTime(String longTime, String dateFormat) {
        if (StringUtils.isEmpty(longTime)) {
            return "";
        }
        return formatTime(Long.parseLong(longTime), dateFormat);
    }

    /**
     * 将时间以00：00：00格式输出
     *
     * @param mss 毫秒数
     * @return string
     */
    public static String formatDuring(String mss) {
        if (StringUtils.isEmpty(mss)) {
            return "00:00:00";
        } else {
            return formatDuring(Long.parseLong(mss));
        }
    }

    public static String formatDuring(long mss) {
        long hours = mss / hourLevelValue;    // 时
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));    // 从00开始算
        if (hours <= 0) {
            return "00:" + format.format(mss);
        } else if (hours > 0 && hours < 10) {
            return "0" + hours + ":" + format.format(mss);
        } else {
            return hours + ":" + format.format(mss);
        }
    }

}

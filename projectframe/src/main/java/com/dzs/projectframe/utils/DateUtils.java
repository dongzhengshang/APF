package com.dzs.projectframe.utils;

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

    public enum TimeType {TODAY, WEEK, LONGTIME}


    /**
     * 计算星座
     *
     * @param month 月
     * @param day   天
     * @return String
     */
    public static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
    }

    public static String getTimeStamp() {
        return String.valueOf(new Timestamp(System.currentTimeMillis()));//
    }

    private final static ThreadLocal<SimpleDateFormat> dataFormat_Minuts = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATEFORMAT_WITHMINUTS);
        }
    };
    private final static ThreadLocal<SimpleDateFormat> dataFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DATEFORMAT);
        }
    };

    /**
     * 获取系统时间
     *
     * @param format 格式
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }


    /**
     * 将日期以  yyyy-MM-dd HH:mm格式输出
     */
    public static String getTime(Date date) {
        if (date == null) {
            return "";
        }
        return dataFormat_Minuts.get().format(date);
    }

    /**
     * 将日期以  yyyy-MM-dd 格式输出
     *
     * @param longTime 时间字符串（System.currentTimeMillis()格式）
     */
    public static String formatTime(String longTime) {
        if (StringUtils.isEmpty(longTime))
            return "";
        return formatTime(Long.parseLong(longTime));
    }

    /**
     * 将日期以yyyy-MM-dd 格式输出
     */
    public static String formatTime(long longTime) {
        return dataFormat.get().format(longTime);
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
     * @param dateFormat 日期格式     * @return String
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

    /**
     * 将时间以00:00:00格式输出
     *
     * @param mss 剩余毫秒数
     * @return 天：时：分
     */
    public static String formatDuring2(long mss) {
        String day, hour, minute, second;
        if (mss <= 0) {
            return "00:00:00";
        }
        int days = (int) (mss / dayLevelValue);
        int hours = (int) ((mss - days * dayLevelValue) / hourLevelValue);
        int minutes = (int) ((mss - (days * dayLevelValue) - (hours * hourLevelValue)) / minuteLevelValue);
        int seconds = (int) ((mss - (days * dayLevelValue) - (hours * hourLevelValue) - (minutes * minuteLevelValue)) / secondLevelValue);
        if (days >= 0 && days < 10) {
            day = "0" + days;
        } else {
            day = "" + days;
        }
        if (hours >= 0 && hours < 10) {
            hour = "0" + hours;
        } else {
            hour = "" + hours;
        }
        if (minutes >= 0 && minutes < 10) {
            minute = "0" + minutes;
        } else {
            minute = "" + minutes;
        }
        if (seconds >= 0 && seconds < 10) {
            second = "0" + seconds;
        } else {
            second = "" + seconds;
        }
        return day + ":" + hour + ":" + minute;
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
     * 获取时间差
     */
    public static TimeType getMyTime(long time) {
        TimeType describe;
        int shiJianCha = (int) (getTodayStart() - time);
        if (time >= getTodayStart()) {
            describe = TimeType.TODAY;
        } else if (shiJianCha > 0 && shiJianCha < dayLevelValue * 7) {//昨天到七天之间
            describe = TimeType.WEEK;
        } else {
            describe = TimeType.LONGTIME;
        }
        return describe;
    }

    /**
     * 获取时间
     *
     * @param time 时间（long）
     * @return
     */
    public static String getTime(long time) {
        int shiJianCha = (int) (getTodayStart() - time);
        if (time >= getTodayStart()) {
            return "今天";
        } else {
            return dataFormat.get().format(time);
        }
    }

}

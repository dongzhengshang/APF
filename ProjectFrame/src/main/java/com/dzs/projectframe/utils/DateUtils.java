package com.dzs.projectframe.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
        cal.set(Calendar.HOUR_OF_DAY, 0);
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
        return formatTime(Long.parseLong(longTime.length() < 13 ? longTime + "000" : longTime), dateFormat);
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
     * 通过年份和月份 得到当月的日子
     *
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
        month++;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return -1;
        }
    }

    /**
     * 返回当前月份1号位于周几
     *
     * @param year  年份
     * @param month 月份，传入系统获取的，不需要正常的
     * @return 日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, 1);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }


    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }

    //获取后面几天日期
    public static Date getNextDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +i);
        date = calendar.getTime();
        return date;
    }

   /* *//*判断是否为当天*//*
    public static boolean isToday(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(new Date(time));
        return calendar.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
    }*/

    public static boolean isToday(long time){
        SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        if(fmt.format(date).toString().equals(fmt.format(new Date()).toString())){//格式化为相同格式
            return true;
        }else {
            return false;
        }
    }

    /**
     * 获取日期
     *
     * @param year
     * @param month
     * @param day
     * @return yyyy-mm-dd
     */
    public static String getDate(String year, String month, String day) {
        DecimalFormat mFormat = new DecimalFormat("00");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(year);
        stringBuffer.append("-");
        stringBuffer.append(mFormat.format(Double.valueOf(month)));
        stringBuffer.append("-");
        stringBuffer.append(mFormat.format(Double.valueOf(day)));
        stringBuffer.append(" ");
        return stringBuffer.toString();
    }

    /**
     * 指定日期是否晚于当前日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static boolean isTimeAfter(String year, String month, String day) {
        Date nowdate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d;
        try {
            d = sdf.parse(getDate(year, month, day));
            boolean flag = d.before(nowdate);
            if (flag) {
                return false;
            } else {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 格式化日期
     *
     * @param date 20170518010203
     * @return 2017-05-18 01:02:03
     */
    public static String getDateFormat(String date) {
        if (TextUtils.isEmpty(date.trim())) return "";
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(date.substring(0, 4)).append("-");
        stringBuffer.append(date.substring(4, 6)).append("-");
        stringBuffer.append(date.substring(6, 8)).append(" ");
        stringBuffer.append(date.substring(8, 10)).append(":");
        stringBuffer.append(date.substring(10, 12)).append(":");
        stringBuffer.append(date.substring(12, date.length()));
        return stringBuffer.toString();
    }

    /**
     * 时间戳转时间
     */
    public static String timestampToTime(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(timestamp * 1000);
    }


    public static String getYear() {
        Calendar calendar = Calendar.getInstance();
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    public static String getMonth() {
        Calendar calendar = Calendar.getInstance();
        DecimalFormat mFormat = new DecimalFormat("00");
        return mFormat.format(Double.valueOf(calendar.get(Calendar.MONTH) + 1));
    }

    public static String getDay() {
        Calendar calendar = Calendar.getInstance();
        DecimalFormat mFormat = new DecimalFormat("00");
        return mFormat.format(Double.valueOf(calendar.get(Calendar.DATE)));
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static String getCurrentData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * Date或者String转化为时间戳
     *
     * @param data
     * @return
     */
    public static long dataToTimestamp(String data) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() / 1000;
    }

    /**
     * 判断某一时间是否在一个区间内
     *
     * @param sourceTime 时间区间,半闭合,如[10:00-20:00)
     * @param curTime    需要判断的时间 如10:00
     * @return
     * @throws IllegalArgumentException
     */
    public static boolean isInTime(String sourceTime, String curTime) {
        if (sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
        if (curTime == null || !curTime.contains(":")) {
            throw new IllegalArgumentException("Illegal Argument arg:" + curTime);
        }
        String[] args = sourceTime.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            long now = sdf.parse(curTime).getTime();
            long start = sdf.parse(args[0]).getTime();
            long end = sdf.parse(args[1]).getTime();
            if (args[1].equals("00:00")) {
                args[1] = "24:00";
            }
            if (end < start) {
                if (now >= end && now < start) {
                    return false;
                } else {
                    return true;
                }
            } else {
                if (now >= start && now < end) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Illegal Argument arg:" + sourceTime);
        }
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 根据时间戳获取当天秒数
     *
     * @param imestamp 日期
     * @returnt
     */
    public static int getSeconds(long imestamp) {
        String time =  getDateToString(imestamp,"yyyy-MM-dd HH:mm:ss");
        String [] times = time.split(" ")[1].split(":");
        int hour = Integer.parseInt(times[0]);
        int minute = Integer.parseInt(times[1]);
        int seconds = Integer.parseInt(times[2]);
        return hour * 3600 + minute * 60 + seconds;
    }

    /**
     * 获取时间
     *
     * @param totalTime 秒数
     * @return 00:00
     */
    public static String getTime(int totalTime) {
        DecimalFormat mFormat = new DecimalFormat("00");
        String min = mFormat.format(totalTime / 60);
        String second = mFormat.format(totalTime % 60);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(min).append(":").append(second);
        return stringBuffer.toString();
    }

}

package com.dzs.projectframe.utils;

import android.util.Log;

import com.dzs.projectframe.Cfg;

/**
 * LOG 工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.2
 * @date 2015年11月21日 下午5:37:28
 */
public class LogUtils {
    public static boolean IS_SHOWLOG = Cfg.IS_DEBUG;

    public static void info(String message) {
        logI("Debug-I", message);
    }

    public static void info(Object message) {
        info(message + "");
    }

    public static void info(String msg, Object... format) {
        info(String.format(msg, format));
    }

    public static void debug(String message) {
        logD("Debug-D", message);
    }

    public static void debug(Object message) {
        debug(message + "");
    }

    public static void debug(String msg, Object... format) {
        debug(String.format(msg, format));
    }

    public static void error(String message) {
        logE("Debug-E", message);
    }

    public static void error(Object message) {
        error(message + "");
    }

    public static void error(String msg, Object... format) {
        error(String.format(msg, format));
    }

    public static void exception(Exception e) {
        if (IS_SHOWLOG && e != null) {
            e.printStackTrace();
        }
    }

    public static void logE(String tag, String message) {
        if (IS_SHOWLOG) {
            Log.e(tag, message);
        }
    }

    public static void logD(String tag, String message) {
        if (IS_SHOWLOG) {
            Log.d(tag, message);
        }
    }

    public static void logI(String tag, String message) {
        if (IS_SHOWLOG) {
            Log.i(tag, message);
        }
    }

}

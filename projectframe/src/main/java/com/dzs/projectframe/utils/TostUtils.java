package com.dzs.projectframe.utils;


import android.content.Context;
import android.widget.Toast;

import com.dzs.projectframe.base.ProjectContext;

/**
 * Tost工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.1
 * @date 2015-12-23 上午9:53:00
 */
public class TostUtils {
    private static String oldMsg;
    private static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    /**
     * 多次触发只显示一次Toast
     */
    public static void showOneToast(Context context, String str, Toast t) {
        if (StringUtils.isEmpty(str)) {
            return;
        }
        if (toast == null) {
            oneTime = System.currentTimeMillis();
            toast = t == null ? createToast(context, str) : t;
        } else {
            twoTime = System.currentTimeMillis();
            if (str.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) toast.show();
            } else {
                toast.setText(str);
                toast.show();
            }
        }
        oldMsg = str;
        oneTime = twoTime;

    }

    /**
     * 多次触发只显示一次Toast
     *
     * @param strId ID
     */
    public static void showOneToast(int strId) {
        showOneToast(ProjectContext.appContext, ProjectContext.appContext.getString(strId), null);
    }

    public static void showOneToast(String str) {
        showOneToast(ProjectContext.appContext, str, null);
    }

    /**
     * 创建Toast
     *
     * @param str 显示的字符串
     */
    private static Toast createToast(Context context, String str) {
        return Toast.makeText(context, str, Toast.LENGTH_SHORT);
    }

    /**
     * 取消
     */
    public static void cancle() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

}

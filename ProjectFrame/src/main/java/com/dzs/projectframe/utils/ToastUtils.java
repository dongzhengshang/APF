package com.dzs.projectframe.utils;


import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.dzs.projectframe.base.ProjectContext;

/**
 * Toast工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.1
 * @date 2015-12-23 上午9:53:00
 */
public class ToastUtils {
    private static String oldMsg;
    private static Toast toast = null;
    private static long lastTime = 0;

    /**
     * 多次触发只显示一次Toast
     */
    public static void showOneToast(Context context, String str) {
        if (TextUtils.isEmpty(str)) return;
        if (toast == null) {
            toast = createToast(context, str);
            lastTime = System.currentTimeMillis();
            toast.show();
        } else {
            if (str.equals(oldMsg)) {
                if (System.currentTimeMillis() - lastTime > Toast.LENGTH_LONG) toast.show();
            } else {
                toast.setText(str);
                toast.show();
            }
            lastTime = System.currentTimeMillis();
        }
        oldMsg = str;
    }

    /**
     * 多次触发只显示一次Toast
     *
     * @param strId ID
     */
    public static void showOneToast(int strId) {
        showOneToast(ProjectContext.appContext, ProjectContext.appContext.getString(strId));
    }

    public static void showOneToast(String str) {
        showOneToast(ProjectContext.appContext, str);
    }

    /**
     * 创建Toast
     *
     * @param str 显示的字符串
     */
    private static Toast createToast(Context context, String str) {
        return Toast.makeText(context, str, Toast.LENGTH_SHORT);
    }

    public static void setToast(Toast t) {
        toast = t;
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

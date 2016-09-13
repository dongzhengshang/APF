package com.dzs.projectframe.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.dzs.projectframe.Conif;

import java.util.Map;

/**
 * SharedPreferences工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version 1.2
 * @date 2015-3-23 下午5:58:36
 */

public class SharedPreferUtils {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static SharedPreferUtils sharedPreferHelper;

    private SharedPreferUtils(Context context) {
        prefs = context.getSharedPreferences(Conif.SHAREDPREFER_USERINFO, Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }

    /**
     * 单利模式
     *
     * @param context 上下文
     * @return SharepreferUtils对象
     */
    public static SharedPreferUtils getInstanse(Context context) {
        if (sharedPreferHelper == null) {
            synchronized (SharedPreferUtils.class) {
                if (sharedPreferHelper == null) {
                    sharedPreferHelper = new SharedPreferUtils(context);
                }
            }
        }
        return sharedPreferHelper;
    }

    /**
     * 保存字符串
     *
     * @param key
     * @param value
     * @return boolean
     */
    public boolean putString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }
    public void putString2(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 获取字符串
     *
     * @param key
     * @return boolean
     */
    public String getString(String key) {
        return prefs.getString(key, null);
    }

    /**
     * 保存int类型
     *
     * @param key
     * @param value
     * @return boolean
     */
    public boolean putInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }
    public void putInt2(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 获取int类型
     *
     * @param key
     * @return int
     */
    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }

    /**
     * 保存boolean类型
     *
     * @param key
     * @param value
     * @return int
     */
    public boolean putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }
    public void putBoolean2(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 获取boolean类型
     *
     * @param key
     * @return boolean
     */
    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    /**
     * 保存map集合
     *
     * @param map
     */
    public void saveMap(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                editor.putString(entry.getKey(), null);
            } else {
                editor.putString(entry.getKey(), entry.getValue().toString());
            }
        }
        editor.commit();
    }
    public void saveMap2(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() == null) {
                editor.putString(entry.getKey(), null);
            } else {
                editor.putString(entry.getKey(), entry.getValue().toString());
            }
        }
        editor.apply();
    }


    /**
     * 清空数据
     *
     * @return boolean
     */
    public boolean clear() {
        editor.clear();
        return editor.commit();
    }

    /**
     * 关闭
     */
    public void close() {
        editor = null;
        prefs = null;
    }

}

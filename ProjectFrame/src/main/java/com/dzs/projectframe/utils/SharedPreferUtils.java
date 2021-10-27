package com.dzs.projectframe.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.dzs.projectframe.Cfg;

import java.util.Map;

/**
 * SharedPreferences工具类
 *
 * @author DZS dzsk@outlook.com
 * @version 1.2
 * @date 2015-3-23 下午5:58:36
 */
public class SharedPreferUtils {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static SharedPreferUtils sharedPreferHelper;

    private SharedPreferUtils(Context context) {
        prefs = context.getSharedPreferences(Cfg.SHARED_PREFER_APPLICATION, Context.MODE_PRIVATE);
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
     * @param key   key
     * @param value value
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
     * @param key key
     * @return boolean
     */
    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    /**
     * 保存int类型
     *
     * @param key   key
     * @param value value
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
     * @param key kye
     * @return int
     */
    public int getInt(String key) {
        return prefs.getInt(key, -1);
    }

    /**
     * 保存boolean类型
     *
     * @param key   key
     * @param value value
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
     * @param key 键值
     * @return boolean
     */
    public boolean getBoolean(String key) {
        return prefs.getBoolean(key, false);
    }

    /**
     * 保存map集合(同步方法)
     *
     * @param map map
     */
    public boolean saveMapByCommit(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object o = entry.getValue();
            if (o == null) {
                editor.putString(entry.getKey().toString(), null);
            } else if (o instanceof Integer) {
                editor.putInt(entry.getKey().toString(), (Integer) o);
            } else if (o instanceof String) {
                editor.putString(entry.getKey().toString(), (String) o);
            } else if (o instanceof Boolean) {
                editor.putBoolean(entry.getKey().toString(), (Boolean) o);
            } else if (o instanceof Long) {
                editor.putLong(entry.getKey().toString(), (Long) o);
            } else if (o instanceof Float) {
                editor.putFloat(entry.getKey().toString(), (Float) o);
            }
        }
        return editor.commit();
    }

    /**
     * 异步保存map集合,不关心是否保存成功
     *
     * @param map map
     */
    public void saveMap(Map<?, ?> map) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object o = entry.getValue();
            if (o == null) {
                editor.putString(entry.getKey().toString(), null);
            } else if (o instanceof Integer) {
                editor.putInt(entry.getKey().toString(), (Integer) o);
            } else if (o instanceof String) {
                editor.putString(entry.getKey().toString(), (String) o);
            } else if (o instanceof Boolean) {
                editor.putBoolean(entry.getKey().toString(), (Boolean) o);
            } else if (o instanceof Long) {
                editor.putLong(entry.getKey().toString(), (Long) o);
            } else if (o instanceof Float) {
                editor.putFloat(entry.getKey().toString(), (Float) o);
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

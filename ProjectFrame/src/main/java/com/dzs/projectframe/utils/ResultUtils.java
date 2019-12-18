package com.dzs.projectframe.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultUtils {

    /**
     * 从结果中获取 Map对象
     */
    public static Map<?, ?> getMapFromResult(Map<?, ?> result, String key) {
        Object object = getObject(result, key);
        if (object instanceof Map) return (Map) object;
        return new HashMap<>();
    }

    /**
     * 从结果中获取ArrayList
     */
    public static ArrayList<?> getListFromResult(Map<?, ?> result, String key) {
        Object object = getObject(result, key);
        if (object instanceof List) return (ArrayList<?>) object;
        return new ArrayList<>();
    }

    /**
     * 从结果中获取字符串
     */
    public static String getStringFromResult(Map<?, ?> result, String key) {
        return getStringFromResult(result, key, "");
    }

    /**
     * 从结果中获取字符串
     *
     * @param result       map集合
     * @param key          键值
     * @param defaultValue 默认值
     */
    public static String getStringFromResult(Map<?, ?> result, String key, String defaultValue) {
        Object object = getObject(result, key);
        if (object != null) {
            return object.toString();
        }
        return defaultValue;
    }

    /**
     * 从结果中获取集合
     *
     * @param result map
     * @param key    map 键
     * @param clazz  类
     * @param <T>    泛形
     * @return ArrayList<T>
     */
    public static <T> ArrayList<T> getListBeanFromResult(Map<?, ?> result, String key, Class<T> clazz) {
        ArrayList<T> ts = new ArrayList<>();
        Object value = getObject(result, key);
        if (value != null) {
            for (Object map : (ArrayList) value) {
                ts.add(clazz.cast(mapToBean((Map) map, clazz)));
            }
        }
        return ts;
    }

    public static <T> ArrayList<T> getListStringBeanFromResult(Map<?, ?> result, String key, Class<T> clazz) {
        ArrayList<T> ts = new ArrayList<>();
        Object value = getObject(result, key);
        if (value != null) {
            for (Object map : (ArrayList) value) {
                ts.add(clazz.cast(mapToStringBean((Map) map, clazz)));
            }
        }
        return ts;
    }

    /**
     * 从结果中获取数据Bean
     *
     * @return T
     */
    public static <T> T getBeanFromResult(Map<?, ?> result, String key, Class<T> clazz) {
        Object object = getObject(result, key);
        if (object != null) {
            return mapToBean((Map) object, clazz);
        }
        return null;
    }


    /**
     * 递归获取数据
     *
     * @param result map集合
     * @param key    键值
     * @return Object
     */
    private static Object getObject(Map<?, ?> result, String key) {
        if (result == null || result.isEmpty() || TextUtils.isEmpty(key)) return null;
        if (result.containsKey(key)) {
            return result.get(key);
        } else {
            for (Map.Entry entry : result.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Map) {
                    Object o = getObject((Map) entry.getValue(), key);
                    if (o != null) return o;
                } else if (entry.getValue() != null && entry.getValue() instanceof List) {
                    Object o2 = getObjectFromCollection((List) entry.getValue(), key);
                    if (o2 != null) return o2;
                }
            }
        }
        return null;
    }

    /**
     * 递归获取数据
     */
    private static Object getObjectFromCollection(Collection collection, String key) {
        if (collection == null || collection.isEmpty() || TextUtils.isEmpty(key)) return null;
        for (Object tempO : collection) {
            if (tempO instanceof Map) {
                Object o2 = getObject((Map) tempO, key);
                if (o2 != null) return o2;
            } else if (tempO instanceof Collection) {
                Object o3 = getObjectFromCollection((Collection) tempO, key);
                if (o3 != null) return o3;
            }
        }
        return null;
    }

    /**
     * 将map反射成数据Bean
     *
     * @param value    需要反射的Map数据
     * @param clazz    反射后的类
     * @param isString 是否将数据Bean中的字段全部置为String
     */
    private static <T> T mapToBean(Map<?, ?> value, Class<T> clazz, boolean isString) {
        if (value == null || clazz == null) return null;
        T t;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            LogUtils.exception(e);
            return null;
        }
        for (Map.Entry map : value.entrySet()) {
            Field field = ReflectionUtils.getInstance().getField(clazz, clazz.toString() + map.getKey());
            if (null != field && map.getValue() instanceof Map) {
                ReflectionUtils.getInstance().setValue(t, field, mapToBean((Map) map.getValue(), field.getType(), isString));
            } else if (null != field && map.getValue() instanceof Collection) {
                ReflectionUtils.getInstance().setValue(t, field, parseList((ArrayList<?>) map.getValue(), field, isString));
            } else if (null != field) {
                ReflectionUtils.getInstance().setValue(t, field, isString ? map.getValue() + "" : map.getValue());
            }
        }
        return t;
    }

    /**
     * 解析集合情况
     */
    private static ArrayList parseList(ArrayList<?> arrayList, Field field, boolean isString) {
        ArrayList<Object> tempList = new ArrayList<>();
        //此处获取泛型的最深处类型
        Class<?> itemType = ReflectionUtils.getInstance().getListItemTypeCls(field);
        for (Object o : arrayList) {
            if (o instanceof Map) {
                tempList.add(mapToBean((Map) o, itemType, isString));
            } else if (o instanceof Collection) {
                tempList.add(parseList((ArrayList<?>) o, field, isString));
            } else {
                tempList.add(o);
            }
        }
        return tempList;
    }

    /**
     * 将MAP转换成数据Bean，并且字段全部为String
     */
    public static <T> T mapToStringBean(Map<?, ?> value, Class<T> clazz) {
        return mapToBean(value, clazz, true);
    }

    /**
     * 将MAP转换成数据Bean
     */
    public static <T> T mapToBean(Map<?, ?> value, Class<T> clazz) {
        return mapToBean(value, clazz, false);
    }

}

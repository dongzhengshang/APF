package com.dzs.projectframe.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultUtils {

    /**
     * 从结果中获取ArrayList
     *
     * @param result map
     * @param key    键
     * @return ArrayList
     */
    public static ArrayList getListMapFromResult(Map<?, ?> result, String key) {
        Object object = getObject(result, key);
        if (object != null) {
            try {
                return ArrayList.class.cast(object);
            }catch (Exception e){
                return new ArrayList<>();
            }

        }
        return new ArrayList<>();
    }

    /**
     * 从结果中获取 Map
     *
     * @param result map
     * @param key    键
     * @return ArrayList
     */
    public static Map<?, ?> getMapFromResult(Map<?, ?> result, String key) {
        Object object = getObject(result, key);
        if (object instanceof Map) return Map.class.cast(object);
        return new HashMap<>();
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
            for (Object map : ArrayList.class.cast(value)) {
                ts.add(clazz.cast(getBeanFromMap(Map.class.cast(map), clazz)));
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
            Map<?, ?> value = Map.class.cast(object);
            return getBeanFromMap(value, clazz);
        }
        return null;
    }

    /**
     * 从结果中获取字符串
     *
     * @param result map集合
     * @param key    键值
     * @return String
     */
    public static String getStringFromResult(Map<?, ?> result, String key) {
        Object object = getObject(result, key);
        if (object != null) {
            return object.toString();
        }
        return "";
    }

    /**
     * 递归
     *
     * @param result map集合
     * @param key    键值
     * @return Object
     */
    private static Object getObject(Map<?, ?> result, String key) {
        if (result == null || result.isEmpty() || StringUtils.isEmpty(key)) return null;
        if (result.containsKey(key)) return result.get(key);
        else {
            for (Map.Entry<?, ?> entry : result.entrySet()) {
                if (entry.getValue() instanceof Map) {
                    Object o = getObject(Map.class.cast(entry.getValue()), key);
                    if (o != null) return o;
                } else if (entry.getValue() instanceof List) {
                    for (Object map : List.class.cast(entry.getValue())) {
                        Object o2 = getObject(Map.class.cast(map), key);
                        if (o2 != null) return o2;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 将map反射成数据Bean
     *
     * @return T
     */

    public static <T> T getBeanFromMap(Map<?, ?> value, Class<T> clazz) {
        if (value == null || clazz == null) return null;
        T t;
        try {
            t = clazz.newInstance();
        } catch (Exception e) {
            LogUtils.exception(e);
            return null;
        }
        for (Map.Entry<?, ?> map : value.entrySet()) {
            Method method = ReflectionUtils.getInstance().getMethod(clazz, clazz.toString() + map.getKey());
            if (null != method && map.getValue() instanceof Map) {
                Class<?> cls = ReflectionUtils.getInstance().getTypeClass(clazz, map.getKey() + "");
                ReflectionUtils.getInstance().setValue(method, t, getBeanFromMap(Map.class.cast(map.getValue()), cls));
            } else if (null != method && map.getValue() instanceof List) {
                Class<?> cls = ReflectionUtils.getInstance().getListItemType(clazz, map.getKey() + "");
                ArrayList ts = new ArrayList<>();
                for (Object o : ArrayList.class.cast(map.getValue())) {
                    ts.add(getBeanFromMap(Map.class.cast(o), cls));
                }
                ReflectionUtils.getInstance().setValue(method, t, ts);
            } else if (null != method) {
                ReflectionUtils.getInstance().setValue(method, t, map.getValue());
            }
        }
        return t;
    }

}

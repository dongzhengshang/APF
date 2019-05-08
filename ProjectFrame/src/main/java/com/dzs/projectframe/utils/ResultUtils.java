package com.dzs.projectframe.utils;

import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultUtils {
	
	/**
	 * 从结果中获取 Map
	 *
	 * @param result map
	 * @param key    键
	 * @return ArrayList
	 */
	public static Map<?, ?> getMapFromResult(Map<?, ?> result, String key) {
		Object object = getObject(result, key);
		if (object instanceof Map) return (Map) object;
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
			for (Object map : (ArrayList) value) {
				ts.add(clazz.cast(getBeanFromMap((Map) map, clazz)));
			}
		}
		return ts;
	}
	
	public static <T> ArrayList<T> getListStringBeanFromResult(Map<?, ?> result, String key, Class<T> clazz) {
		ArrayList<T> ts = new ArrayList<>();
		Object value = getObject(result, key);
		if (value != null) {
			for (Object map : (ArrayList) value) {
				ts.add(clazz.cast(getStringBeanFromMap((Map) map, clazz)));
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
			Map<?, ?> value = (Map) object;
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
	 * 从结果中获取字符串集合
	 *
	 * @param result map集合
	 * @param key    键值
	 * @return String
	 */
	public static ArrayList getStringList(Map<?, ?> result, String key) {
		Object object = getObject(result, key);
		if (object instanceof List) {
			return (ArrayList) object;
		}
		return new ArrayList<>();
	}
	
	/**
	 * 从结果中获取字符串
	 *
	 * @param result       map集合
	 * @param key          键值
	 * @param defaultValue 默认值
	 * @return String
	 */
	public static String getStringFromResult(Map<?, ?> result, String key, String defaultValue) {
		Object object = getObject(result, key);
		if (object != null && !TextUtils.isEmpty(object.toString())) {
			return object.toString();
		}
		return defaultValue;
	}
	
	/**
	 * 递归
	 *
	 * @param result map集合
	 * @param key    键值
	 * @return Object
	 */
	private static Object getObject(Map<?, ?> result, String key) {
		if (result == null || result.isEmpty() || TextUtils.isEmpty(key)) {
			return null;
		}
		if (result.containsKey(key)) {
			return result.get(key);
		} else {
			for (Map.Entry<?, ?> entry : result.entrySet()) {
				if (entry.getValue() instanceof Map) {
					Object o = getObject((Map) entry.getValue(), key);
					if (o != null) {
						return o;
					}
				} else if (entry.getValue() != null && entry.getValue() instanceof List) {
					for (Object va : (List) entry.getValue()) {
						if (va instanceof Map) {
							Object o2 = getObject((Map) va, key);
							if (o2 != null) {
								return o2;
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 将map反射成数据Bean
	 *
	 * @param value    需要反射的Map数据
	 * @param clazz    反射后的类
	 * @param isString 字段是否为String
	 */
	private static <T> T getBeanFromMap(Map<?, ?> value, Class<T> clazz, boolean isString) {
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
				ReflectionUtils.getInstance().setValue(method, t, getBeanFromMap((Map) map.getValue(), cls, isString));
			} else if (null != method && map.getValue() instanceof List) {
				Class<?> cls = ReflectionUtils.getInstance().getListItemType(clazz, map.getKey() + "");
				ArrayList ts = new ArrayList<>();
				if (map.getValue() != null) {
					for (Object o : (ArrayList) map.getValue()) {
						ts.add(getBeanFromMap((Map) o, cls, isString));
					}
				}
				
				ReflectionUtils.getInstance().setValue(method, t, ts);
			} else if (null != method) {
				ReflectionUtils.getInstance().setValue(method, t, isString ? map.getValue() + "" : map.getValue());
			}
		}
		return t;
	}
	
	public static <T> T getStringBeanFromMap(Map<?, ?> value, Class<T> clazz) {
		return getBeanFromMap(value, clazz, true);
	}
	
	public static <T> T getBeanFromMap(Map<?, ?> value, Class<T> clazz) {
		return getBeanFromMap(value, clazz, false);
	}
	
}

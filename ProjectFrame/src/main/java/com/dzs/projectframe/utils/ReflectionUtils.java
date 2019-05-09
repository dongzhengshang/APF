package com.dzs.projectframe.utils;

import com.dzs.projectframe.interf.SetValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 反射工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-11-30 上午10:01:41
 */
public class ReflectionUtils {
	
	private static Map<String, Field> fieldPool = new Hashtable<>();
	private static ReflectionUtils reflectionUtils;
	
	private ReflectionUtils() {
	}
	
	public static ReflectionUtils getInstance() {
		if (reflectionUtils == null) {
			synchronized (ReflectionUtils.class) {
				if (reflectionUtils == null) {
					reflectionUtils = new ReflectionUtils();
				}
			}
		}
		return reflectionUtils;
	}
	
	/**
	 * 获取字段
	 */
	public Field getField(Class<?> clazz, String key) {
		if (fieldPool.containsKey(key)) {
			return fieldPool.get(key);
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			SetValue b = f.getAnnotation(SetValue.class);
			if (null != b) {
				String value = clazz.toString() + b.value();
				fieldPool.put(value, f);
				if (value.equals(key)) return f;
			}
		}
		return null;
	}
	
	/**
	 * 设置数据
	 */
	public void setValue(Object cls, Field field, Object value) {
		try {
			field.setAccessible(true);
			field.set(cls, value);
		} catch (Exception e) {
			LogUtils.error("Setting data exception" + e);
		}
	}
	
	/**
	 * 获取字段类型
	 *
	 * @param clazz      class
	 * @param fieldsName 字段名
	 * @return object
	 */
	public Class getFieldTypeClass(Class<?> clazz, String fieldsName) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Class cls = field.getType();
			if (field.getName().equals(fieldsName)) return cls;
		}
		return null;
	}
	
	/**
	 * 获取泛型T的类型 T.class
	 */
	public Class<?> getTClass(Class<?> clazz) {
		Type sType = clazz.getGenericSuperclass();
		Type[] generics = new Type[0];
		if (sType != null) {
			generics = ((ParameterizedType) sType).getActualTypeArguments();
		}
		return (Class<?>) generics[0];
	}
	
	/**
	 * 获取集合泛型参数类型
	 *
	 * @param clazz      class
	 * @param fieldsName 字段名
	 * @return object
	 */
	public Class<?> getListItemType(Class<?> clazz, String fieldsName) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(List.class)) {
				Type fc = field.getGenericType();
				if (fc == null) continue;
				if (fc instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) fc;
					return (Class) pt.getActualTypeArguments()[0];
				}
			}
		}
		return null;
	}
}
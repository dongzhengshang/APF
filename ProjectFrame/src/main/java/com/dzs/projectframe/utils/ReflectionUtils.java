package com.dzs.projectframe.utils;

import com.dzs.projectframe.interf.DataBean;

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
	
	private static Map<String, Method> methodPool = new Hashtable<>();
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
	
	public void setValue(Method method, Object object, Object value) {
		try {
			method.invoke(object, value);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public Method getMethod(Class<?> clazz, String key) {
		if (methodPool.containsKey(key)) {
			return methodPool.get(key);
		}
		Method[] methods = clazz.getMethods();
		for (Method m : methods) {
			DataBean b = m.getAnnotation(DataBean.class);
			if (null != b) {
				String value = clazz.toString() + b.value();
				methodPool.put(value, m);
				if (value.equals(key)) {
					return m;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取字段类型
	 *
	 * @param clazz      class
	 * @param fieldsName 字段名
	 * @return object
	 */
	public Class getTypeClass(Class<?> clazz, String fieldsName) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Class cls = field.getType();
			if (field.getName().equals(fieldsName)) {
				return cls;
			}
		}
		return null;
	}
	
	/**
	 * 获取泛型参数
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
	 * 获取泛型参数类型
	 *
	 * @param clazz      class
	 * @param fieldsName 字段名
	 * @return object
	 */
	public Class<?> getListItemType(Class<?> clazz, String fieldsName) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if (field.getType().isAssignableFrom(List.class)) {
				Type fc = field.getGenericType(); // 关键的地方，如果是List类型，得到其Generic的类型
				if (fc == null) continue;
				if (fc instanceof ParameterizedType) // 【3】如果是泛型参数的类型
				{
					ParameterizedType pt = (ParameterizedType) fc;
					return (Class) pt.getActualTypeArguments()[0];
				}
			}
		}
		return null;
	}
}
package com.dzs.projectframe.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON解析器
 *
 * @author DZS dzsdevelop@163.com
 * @version V2.0
 * @date 2015-12-4 下午4:25:33
 */
public class JsonUtils {
	
	/**
	 * 将Map转换为json字符串
	 *
	 * @param params map
	 * @return json字符串
	 */
	public static String mapToJsonStr(Map<?, ?> params) throws JSONException {
		JSONObject jsonObject = mapToJsonOb(params);
		return jsonObject.toString();
	}
	
	/**
	 * 将Map转换为JSONObject
	 *
	 * @param params map
	 * @return JSONObject
	 */
	public static JSONObject mapToJsonOb(Map<?, ?> params) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		for (Map.Entry<?,?> entry : params.entrySet()) {
			if (entry.getValue() instanceof Map) {
				jsonObject.put((String) entry.getKey(), mapToJsonOb((Map<?,?>) entry.getValue()));
			} else if (entry.getValue() instanceof List) {
				JSONArray jsonArray = new JSONArray();
				List<?> list = (List<?>) entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					if (((List<?>) entry.getValue()).get(i) instanceof Map) {
						jsonArray.put(i, mapToJsonOb((Map<?, ?>) list.get(i)));
					} else {
						jsonArray.put(i, list.get(i));
					}
				}
				jsonObject.put((String) entry.getKey(), jsonArray);
			} else {
				jsonObject.put((String) entry.getKey(), entry.getValue());
			}
		}
		return jsonObject;
	}
	
	
	/**
	 * 将json字符串解析为map
	 *
	 * @param jsonStr json字符串
	 * @return map
	 * @throws JSONException JSON异常
	 */
	public static HashMap<String, Object> jsonToMap(String jsonStr) throws JSONException {
		if (!TextUtils.isEmpty(jsonStr)) {
			JSONObject json = new JSONObject(jsonStr);
			return resolveJsonOb(json);
		}
		return new HashMap<>();
	}
	
	/**
	 * 解析JSONObject
	 */
	private static HashMap<String, Object> resolveJsonOb(JSONObject jsonObject) throws JSONException {
		HashMap<String, Object> result = new HashMap<>();
		if (jsonObject != null) {
			Iterator<String> it = jsonObject.keys();
			while (it.hasNext()) {
				String key = it.next();
				Object value = jsonObject.get(key);
				if (value instanceof JSONObject) {
					result.put(key.trim(), resolveJsonOb((JSONObject) value));
				} else if (value instanceof JSONArray) {
					result.put(key.trim(), resolveJsonA((JSONArray) value));
				} else {
					result.put(key.trim(), value);
				}
			}
		}
		return result;
	}
	
	/**
	 * 解析json数组
	 */
	private static List<?> resolveJsonA(JSONArray jsonArray) throws JSONException {
		if (jsonArray == null) return new ArrayList<>();
		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		ArrayList<Object> objects = new ArrayList<>();
		for (int j = 0; j < jsonArray.length(); j++) {
			Object value = jsonArray.get(j);
			if (value instanceof JSONObject) {
				list.add(resolveJsonOb((JSONObject) value));
			} else if (value instanceof JSONArray) {
				resolveJsonA((JSONArray) value);
			} else {
				objects.add(value);
			}
		}
		return list.size() > objects.size() ? list : objects;
	}
}

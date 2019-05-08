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
	public static String mapToJsonStr(Map<?, ?> params) throws Exception {
		JSONObject jsonObject = mapToJSONObject(params);
		return jsonObject == null ? "" : jsonObject.toString();
	}
	
	/**
	 * 将Map转换为JSONObject
	 *
	 * @param params map
	 * @return JSONObject
	 */
	public static JSONObject mapToJSONObject(Map<?, ?> params) throws Exception {
		JSONObject jsonObject = new JSONObject();
		for (Object o : params.entrySet()) {
			Map.Entry entry = (Map.Entry) o;
			if (entry.getValue() instanceof Map) {
				jsonObject.put((String) entry.getKey(), mapToJSONObject((Map) entry.getValue()));
			} else if (entry.getValue() instanceof List) {
				JSONArray jsonArray = new JSONArray();
				List list = (List) entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					if (((List) entry.getValue()).get(i) instanceof Map) {
						jsonArray.put(i, mapToJSONObject((Map<?, ?>) list.get(i)));
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
	public static HashMap<String, Object> getMap(String jsonStr) throws JSONException {
		HashMap<String, Object> result = new HashMap<>();
		if (!TextUtils.isEmpty(jsonStr)) {
			JSONObject json = new JSONObject(jsonStr);
			Iterator<String> it = json.keys();
			while (it.hasNext()) {
				String key = it.next();
				Object value = json.get(key);
				if (value instanceof JSONArray) {
					result.put(key.trim(), getMapList(value + ""));
				} else if (value instanceof JSONObject) {
					result.put(key.trim(), getMap(value + ""));
				} else if (value instanceof String) {
					result.put(key.trim(), json.getString(key));
				} else if (value instanceof Boolean) {
					result.put(key.trim(), json.getBoolean(key));
				} else if (value instanceof Double) {
					result.put(key.trim(), json.getDouble(key));
				} else if (value instanceof Integer) {
					result.put(key.trim(), json.getInt(key));
				} else if (value instanceof Long) {
					result.put(key.trim(), json.getLong(key));
				} else {
					result.put(key.trim(), value + "");
				}
			}
		}
		return result;
	}
	
	/**
	 * 解析json数组
	 *
	 * @param jsonStr json字符串
	 * @return List
	 */
	private static List<?> getMapList(String jsonStr) throws JSONException {
		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		ArrayList<String> list1 = new ArrayList<>();
		JSONArray ja = new JSONArray(jsonStr);
		for (int j = 0; j < ja.length(); j++) {
			Object value = ja.get(j);
			if (value instanceof JSONObject) {
				HashMap<String, Object> map = getMap(value + "");
				list.add(map);
			} else {
				list1.add(value + "");
			}
		}
		return list.size() > list1.size() ? list : list1;
	}
}

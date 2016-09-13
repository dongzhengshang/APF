package com.dzs.projectframe.utils;

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
        JSONObject jsonObject = new JSONObject();
        for (Object o : params.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            if (entry.getValue() instanceof Map && entry.getValue() != null) {
                jsonObject.put((String) entry.getKey(), mapToJsonStr(Map.class.cast(entry.getValue())));
            } else if (entry.getValue() instanceof List) {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < ((List) entry.getValue()).size(); i++) {
                    jsonArray.put(i, mapToJsonStr((Map<?, ?>) ((List) entry.getValue()).get(i)));
                }
                jsonObject.put((String) entry.getKey(), jsonArray);
            } else {
                jsonObject.put((String) entry.getKey(), entry.getValue());
            }
        }
        return jsonObject.toString();
    }


    /**
     * 将json字符串解析为map
     *
     * @param jsonStr json字符串
     * @return map
     * @throws Exception
     */
    public static Map<String, Object> getMap(String jsonStr) throws JSONException {
        Map<String, Object> result = new HashMap<>();
        if (!StringUtils.isEmpty(jsonStr)) {
            JSONObject json = new JSONObject(jsonStr);
            Iterator<String> it = json.keys();
            while (it.hasNext()) {
                String key = it.next();
                Object value = json.get(key);
                if (value instanceof JSONArray) {
                    result.put(key.trim(), getMapList(value + ""));
                } else if (value instanceof JSONObject) {
                    result.put(key.trim(), getMap(value + ""));
                } else {
                    result.put(key.trim(), value);
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
     * @throws Exception
     */
    public static List<Map<String, Object>> getMapList(String jsonStr) throws JSONException {
        List<Map<String, Object>> list = new ArrayList<>();
        JSONArray ja = new JSONArray(jsonStr);
        for (int j = 0; j < ja.length(); j++) {
            Object value = ja.get(j);
            if (value instanceof JSONObject) {
                Map<String, Object> map = getMap(value + "");
                list.add(map);
            }
        }
        return list;
    }
}

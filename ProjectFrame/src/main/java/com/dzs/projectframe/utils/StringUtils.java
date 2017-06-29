package com.dzs.projectframe.utils;

import android.support.annotation.NonNull;

import com.dzs.projectframe.base.ProjectContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.1
 * @date 2015-12-23 上午9:53:00
 */
public class StringUtils {

    private final static Pattern EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    private final static Pattern PHONE = Pattern.compile("^[1][3,4,5,8][0-9]{9}$");
    private final static Pattern IMG_URL = Pattern.compile(".*?(gif|jpeg|png|jpg|bmp)");
    private final static Pattern URL = Pattern.compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");
    public static final String UTF_8 = "UTF-8";

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email email
     * @return boolean
     */
    public static boolean isEmail(CharSequence email) {
        return !isEmpty(email) && EMAIL.matcher(email).matches();
    }

    /**
     * 判断手机号
     *
     * @param phone 手机号字符串
     * @return boolean
     */
    public static boolean isPhone(CharSequence phone) {
        return !isEmpty(phone) && PHONE.matcher(phone).matches();
    }

    /**
     * 判断字符串是否为空
     *
     * @param input 字符串
     * @return boolean
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || input.length() == 0) return true;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串数组中是否有空
     *
     * @param input 字符串数组
     * @return boolean
     */
    public static boolean isEmpty(CharSequence... input) {
        if (input == null || input.length == 0) return true;
        for (CharSequence in : input) {
            if (isEmpty(in)) return true;
        }
        return true;
    }

    /**
     * 判断是否为url
     *
     * @param str URL支付串
     * @return boolean
     */
    public static boolean isUrl(CharSequence str) {
        return !isEmail(str) && URL.matcher(str).matches();
    }

    /**
     * 判断是否是图片url
     *
     * @param url 图片URL
     * @return boolean
     */
    public static boolean isImgUrl(CharSequence url) {
        return !isEmpty(url) && IMG_URL.matcher(url).matches();
    }

    /**
     * 字符串转换为Unicode编码
     *
     * @param s 字符串
     * @return String
     */
    public static String stringToUnicode(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            if (ch > 255) {
                str.append("\\").append("u").append(Integer.toHexString(ch));
            } else {
                str.append("\\").append(Integer.toHexString(ch));
            }
        }
        return str.toString();
    }

    /**
     * 将网络请求Map转换为url
     *
     * @param url        URL
     * @param parameters 参数列表
     * @return String
     * @throws UnsupportedEncodingException 转换异常
     */
    public static String mapToUrl(String url, Map<String, Object> parameters) throws UnsupportedEncodingException {
        return mapToCatchUrl(url, parameters);
    }

    /**
     * 将网络请求Map转换成为缓存KEY，去掉可变量
     *
     * @param url         URL
     * @param parameters  请求参数
     * @param variableKey 可变的键值(可以为空)
     * @throws UnsupportedEncodingException 异常
     */
    public static String mapToCatchUrl(String url, Map<String, Object> parameters, String... variableKey) throws UnsupportedEncodingException {
        StringBuilder getUrl = new StringBuilder(url);
        if (parameters != null && !parameters.isEmpty()) {
            getUrl.append("?");
            for (Map.Entry<String, Object> param : parameters.entrySet()) {
                if (variableKey != null) {
                    for (String key : variableKey) {
                        if (param.getKey().equals(key)) break;
                    }
                }
                getUrl.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue() == null ? "" : param.getValue().toString(), UTF_8));
                getUrl.append("&");
            }
            getUrl.deleteCharAt(getUrl.length() - 1);
        }
        return getUrl.toString();
    }

    /**
     * 将map请求参数转换为get参数
     *
     * @param parameters 请求参数
     * @return StringBuilder
     * @throws UnsupportedEncodingException 转换异常
     */
    public static StringBuilder mapToParameters(Map<String, Object> parameters) throws UnsupportedEncodingException {
        if (parameters == null || parameters.isEmpty()) return new StringBuilder("");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> param : parameters.entrySet()) {
            sb.append(param.getKey()).append("=").append(URLEncoder.encode(param.getValue() == null ? "" : param.getValue().toString(), UTF_8));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb;
    }

    /**
     * 判断密码是否包含中文
     *
     * @param password 密码字符串
     */
    public static boolean isContainChinese(@NonNull String password) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }
}

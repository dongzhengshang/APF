package com.dzs.projectframe.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
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
     * @param email
     * @return
     */
    public static boolean isEmail(CharSequence email) {
        if (isEmpty(email)) return false;
        return EMAIL.matcher(email).matches();
    }

    /**
     * 判断手机号
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(CharSequence phone) {
        if (isEmpty(phone)) return false;
        return PHONE.matcher(phone).matches();
    }

    /**
     * 判断字符串是否为空
     *
     * @param input
     * @return
     */
    public static boolean isEmpty(CharSequence input) {
        if (input == null || input.length() == 0)
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为url
     *
     * @param str
     * @return
     */
    public static boolean isUrl(CharSequence str) {
        if (isEmail(str)) return false;
        return URL.matcher(str).matches();
    }

    /**
     * 判断是否是图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(CharSequence url) {
        if (isEmpty(url)) return false;
        return IMG_URL.matcher(url).matches();
    }

    /**
     * 字符串转换为Unicode编码
     *
     * @param s
     * @return
     */
    public static String stringToUnicode(String s) {
        StringBuffer str = new StringBuffer();
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
     * @param url
     * @param parmas
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String mapToUrl(String url, Map<String, Object> parmas) throws UnsupportedEncodingException {
        return mapToCachUrl(url, parmas);
    }

    /**
     * 将网络请求Map转换成为缓存KEY，去掉可变量
     *
     * @param url         URL
     * @param parmas      请求参数
     * @param variableKey 可变的键值(可以为空)
     * @throws UnsupportedEncodingException
     */
    public static String mapToCachUrl(String url, Map<String, Object> parmas, String... variableKey) throws UnsupportedEncodingException {
        StringBuilder getUrl = new StringBuilder(url);
        if (parmas != null && !parmas.isEmpty()) {
            getUrl.append("?");
            for (Map.Entry<String, Object> parm : parmas.entrySet()) {
                if (variableKey != null) {
                    for (String key : variableKey) {
                        if (parm.getKey().equals(key)) break;
                    }
                }
                getUrl.append(parm.getKey()).append("=").append(URLEncoder.encode(parm.getValue() == null ? "" : parm.getValue().toString(), UTF_8));
                getUrl.append("&");
            }
            getUrl.deleteCharAt(getUrl.length() - 1);
        }
        return getUrl.toString();
    }

}

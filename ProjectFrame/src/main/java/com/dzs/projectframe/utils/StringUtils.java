package com.dzs.projectframe.utils;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author DZS dzsk@outlook.com
 * @version V1.1
 * @date 2015-12-23 上午9:53:00
 */
public class StringUtils {
	
	private final static Pattern EMAIL = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static Pattern PHONE = Pattern.compile("^[1][3,4,5,6,7,8,9][0-9]{9}$");
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
		return !TextUtils.isEmpty(email) && EMAIL.matcher(email).matches();
	}
	
	/**
	 * 判断手机号
	 *
	 * @param phone 手机号字符串
	 * @return boolean
	 */
	public static boolean isPhone(CharSequence phone) {
		return !TextUtils.isEmpty(phone) && PHONE.matcher(phone).matches();
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
		return !TextUtils.isEmpty(url) && IMG_URL.matcher(url).matches();
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
	 * 判断密码是否包含中文
	 *
	 * @param password 密码字符串
	 */
	public static boolean isContainChinese(@NonNull String password) {
		Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher matcher = pattern.matcher(password);
		return matcher.find();
	}
	
	/**
	 * 字符串转int
	 *
	 * @param text
	 * @return
	 */
	public static int StringToInt(String text) {
		try {
			return Integer.parseInt(text);
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * 字符串转Float
	 *
	 * @param text
	 * @return
	 */
	public static float StringToFloat(String text) {
		try {
			return Float.parseFloat(text);
		} catch (Exception e) {
			return 0.00f;
		}
	}
}

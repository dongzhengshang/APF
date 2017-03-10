package com.dzs.projectframe.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 *
 * @author DZS dzsdevelop@163.com
 * @date 2015-6-2 下午4:10:42
 */
public class EncryptionUtils {

    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS7Padding";
    private static final String UTF_8 = "UTF-8";
    private static final String MD5 = "MD5";
    private static final String AES = "AES";

    /**
     * MD5 32位加密
     *
     * @param data 加密字符串
     * @return String
     */
    public static String MD5encode(String data) {
        return MD5encode(data, false);
    }

    /**
     * @param data       被密数据
     * @param is16Encode 是否是16位加密
     */
    public static String MD5encode(String data, boolean is16Encode) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            md.update(data.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder sb = new StringBuilder();
            for (byte aB : b) {
                i = aB;
                if (i < 0) i += 256;
                if (i < 16) sb.append("0");
                sb.append(Integer.toHexString(i));
            }
            if (is16Encode) {
                result = sb.toString().substring(8, 24);// 16位加密
            } else {
                result = sb.toString();// 32位加密
            }
        } catch (NoSuchAlgorithmException e) {
            LogUtils.exception(e);
        }
        return result;
    }

    //创建AES密钥
    public static String createAESKey() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance(AES);
            kg.init(128);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return byte2hex(b);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.exception(e);
        }
        return "";
    }

    public static byte[] encrypt(byte[] content, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            int blockSize = cipher.getBlockSize();

            int plaintextLength = content.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), AES);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            LogUtils.exception(e);
        }
        return null;
    }

    //加密字符串
    public static String encryptString(String content, String key, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        data = encrypt(data, key, iv);
        return byte2hex(data);
    }

    //解密
    public static String descryptString(String content, String key, String iv) {
        byte[] data = null;
        try {
            data = hex2byte(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = decrypt(data, key, iv);
        if (data == null) return null;
        String result = null;
        try {
            result = new String(data, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    //解密
    public static byte[] decrypt(byte[] content, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), AES);
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(content);
        } catch (Exception e) {
            LogUtils.exception(e);
        }
        return null;
    }

    //16进制字符串转换为二进制数组
    public static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        return hex2byte(inputString.getBytes());
    }

    //16进制转换为二进制
    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) (Integer.parseInt(item, 16) & 0xFF);
        }
        return b2;
    }

    //二进制转换为16进制字符串
    public static String byte2hex(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        String tmp;
        for (byte aB : b) {
            tmp = (Integer.toHexString(aB & 0XFF));
            if (tmp.length() == 1) sb.append("0");
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }
}

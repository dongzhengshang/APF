package com.dzs.projectframe.utils;

import android.annotation.SuppressLint;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 *
 * @author DZS dzsdevelop@163.com
 * @date 2015-6-2 下午4:10:42
 */
public class EncryptionUtils {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS7Padding";
    private static final String UTF_8 = "UTF-8";

    /**
     * DES算法，加密
     *
     * @param data 待加密字符串
     * @param key  加密私钥，长度不能够小于8位
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws InvalidAlgorithmParameterException
     * @throws Exception
     */
    @SuppressLint ("TrulyRandom")
    public static String encodeByDES(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data.getBytes());
            return byte2hex(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * DES算法，解密
     *
     * @param data 待解密字符串
     * @param key  解密私钥，长度不能够小于8位
     * @return 解密后的字节数组
     * @throws Exception 异常
     */
    public static String decodeByDES(String key, String data) {
        if (data == null)
            return null;
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec("12345678".getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return new String(cipher.doFinal(hex2byte(data.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    /**
     * MD5加密16位加密
     *
     * @param data
     */
    public static String MD5encode(String data) {
        return MD5encode(data, false);
    }

    /**
     * @param data      被密数据
     * @param is32Encry 是否是32位加密
     */
    public static String MD5encode(String data, boolean is32Encry) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            if (is32Encry) {
                result = buf.toString().substring(8, 24);// 32位加密
            } else {
                result = buf.toString();// 16位加密
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    //创建AES密钥
    private static SecretKeySpec createKey(String password) {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(32);
        sb.append(password);
        while (sb.length() < 32) {
            sb.append("0");
        }
        if (sb.length() > 32) {
            sb.setLength(32);
        }

        try {
            data = sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, "AES");
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
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    public static String encrypt(String content, String password, String iv) {
//        byte[] data = null;
//        try {
//            data = content.getBytes("UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        data = encrypt(data, password, iv);
//
//        String result = Base64.encode(data);// byte2hex(data);
//        return result;
//    }

    //加密字符串
    public static String encryptString(String content, String key, String iv) {
        byte[] data = null;
        try {
            data = content.getBytes("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        data = encrypt(data, key, iv);
        String result = byte2hex(data);
        return result;
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
        if (data == null)
            return null;
        String result = null;
        try {
            result = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    //解密
    public static byte[] decrypt(byte[] content, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
//    //解密
//    public static String decrypt(String content, String password, String iv) {
//        byte[] data = null;
//        try {
//            data = Base64.decode(content);//hex2byte(content);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        data = decrypt(data, password, iv);
//        if (data == null)
//            return null;
//        String result = null;
//        try {
//            result = new String(data, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }


    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    //二进制转换为16进制字符串
    public static String byte2hex(byte[] b) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    //16进制字符串转换为二进制数组
    public static byte[] hex2byte(String inputString) {
        if (inputString == null || inputString.length() < 2) {
            return new byte[0];
        }
        inputString = inputString.toLowerCase();
        int l = inputString.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = inputString.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

}

package com.dzs.projectframe;

import android.content.pm.PackageInfo;

import com.dzs.projectframe.base.ProjectContext;
import com.dzs.projectframe.utils.SystemUtils;

/**
 * 配置文件
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public class Conif {
    public static boolean IS_DEBUG = true;
    public static String APP_ROOT = "PROJECT";
    public static String SHAREDPREFER_USERINFO = "userinfo";
    // 连接超时
    public static int TIMEOUT_CONNECTION = 3000;
    // 读取超时
    public static int TIMEOUT_READ = 5000;
    // 网络访问次数
    public static int RETRY_TIME = 3;

    public enum HttpResult {
        Success(200, "请求成功"), Faile(201, "网络连接失败"), NetNotConnect(202, "网络连接失败,请检查网络连接"),
        ParseFaile(203, "数据解析失败,请重试");
        private int code;
        private String message;

        HttpResult(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

    public static StringBuilder getUserAgent() {
        PackageInfo info = SystemUtils.getPackageInfo(ProjectContext.appContext);
        return new StringBuilder("DZSDevelop_Android")
                .append("/").append(android.os.Build.MODEL)//手机型号
                .append("/").append(android.os.Build.VERSION.RELEASE)//手机系统版本
                .append("/").append(info.versionName).append("_V").append(info.versionCode);//App版本
    }

    //-------------缓存时间--------------------------//
    private static Long WIFI_CACHE_TIME = (long) (0.5 * 60 * 1000);//半分钟
    private static Long NET_CACHE_TIME = (long) (60 * 1000);//1分钟

    public static Long getCacheTime() {
        int state = SystemUtils.getNetworkType(ProjectContext.appContext);
        if (state == 0) {
            return (long) 0;
        } else if (state == SystemUtils.WIFI) {
            return WIFI_CACHE_TIME;
        } else {
            return NET_CACHE_TIME;
        }
    }
}

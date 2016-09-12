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


    public static String getUserAgent() {
        PackageInfo info = SystemUtils.getPackageInfo(ProjectContext.appContext);
        StringBuilder ua = new StringBuilder("DZSDevelop-Android");
        ua.append('/' + info.versionName + '_' + info.versionCode);// App版本
        ua.append("/Android");// 手机系统平台
        ua.append("/" + android.os.Build.MODEL); // 手机型号
        ua.append("/" + android.os.Build.VERSION.RELEASE);// 手机系统版本
        return ua.toString();
    }

    //-------------缓存时间--------------------------//
    private static Long WIFI_CACHE_TIME = (long) (0.5 * 60 * 1000);//半分钟
    private static Long NET_CACHE_TIME = (long) (1 * 60 * 1000);//1分钟

    public static Long getCacheTime() {
        int state = SystemUtils.getNetworkType(ProjectContext.appContext);
        if (state == 0) {
            return (long) 0;
        } else if (state == SystemUtils.NETTYPE_WIFI) {
            return WIFI_CACHE_TIME;
        } else {
            return NET_CACHE_TIME;
        }
    }
}

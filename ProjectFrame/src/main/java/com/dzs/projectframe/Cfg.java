package com.dzs.projectframe;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;

import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.base.ProjectContext;
import com.dzs.projectframe.utils.StringUtils;
import com.dzs.projectframe.utils.SystemUtils;

import java.io.Serializable;

/**
 * 配置文件
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/19.
 */
public class Cfg {
    public static boolean IS_DEBUG = false;
    public static String APP_ROOT = ProjectContext.appContext.getPackageName();
    public static String SHAREDPREFER_USERINFO = "ApplicationData";
    /**
     * 连接超时时间
     */
    public static int TIMEOUT_CONNECTION = ProjectContext.resources.getInteger(R.integer.TIMEOUT_CONNECTION);
    /**
     * 读取超时
     */
    public static int TIMEOUT_READ = ProjectContext.resources.getInteger(R.integer.TIMEOUT_READ);
    /**
     * 网络访问次数
     */
    public static int RETRY_TIME = ProjectContext.resources.getInteger(R.integer.RETRY_TIME);

    private static String appendUserAgent = "";
    private static StringBuilder userAgent;

    public enum NetResultType {
        NET_CONNECT_SUCCESS(ProjectContext.resources.getString(R.string.NetConnectSuccess)),
        NET_CONNECT_FAIL(ProjectContext.resources.getString(R.string.NetConnectFail)),
        NET_NOT_CONNECT(ProjectContext.resources.getString(R.string.NetNotConnectFail)),
        NET_PARSE_FAIL(ProjectContext.resources.getString(R.string.NetParseFail)),;
        private String message;

        NetResultType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /*操作结果枚举类*/
    public enum OperationResultType {
        SUCCESS(""), FAIL(""),//成功/失败
        PROGRESS(""), FAIL_HAS_CACHE("");//进度/缓存
        private String message;

        OperationResultType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public OperationResultType setMessage(String message) {
            this.message = message;
            return this;
        }
    }

    public interface OperationResult {
        void onResult(OperationResultType type);
    }

    public interface OperationResultWithData {
        void onResult(OperationResultType type, LibEntity libEntity);
    }

    public static StringBuilder getUserAgent() {
        if (StringUtils.isEmpty(userAgent)) {
            PackageInfo info = SystemUtils.getPackageInfo(ProjectContext.appContext);
            return new StringBuilder("DZSDevelop_Android")
                    .append("/").append(android.os.Build.MODEL)//手机型号
                    .append("/").append(android.os.Build.VERSION.RELEASE)//手机系统版本
                    .append("/").append(info.versionName).append("_V").append(info.versionCode)//App版本
                    .append(appendUserAgent);
        } else {
            return userAgent;
        }
    }

    public static void addUserAgent(String userAgent) {
        appendUserAgent = userAgent;
    }

    public static void setUserAgent(StringBuilder stringBuilder) {
        userAgent = stringBuilder;
    }

    //-------------缓存时间--------------------------//
    private static Long WIFI_CACHE_TIME = (long) (ProjectContext.resources.getInteger(R.integer.WIFI_CACHE_TIME) * 60 * 1000);//缓存时间
    private static Long NET_CACHE_TIME = (long) (ProjectContext.resources.getInteger(R.integer.NET_CACHE_TIME) * 60 * 1000);//缓存时间

    public static Long getCacheTime() {
        SystemUtils.NetWorkType networkType = SystemUtils.getNetworkType(ProjectContext.appContext);
        if (networkType.type == SystemUtils.NetWorkType.WIFI.type) {
            return WIFI_CACHE_TIME;
        }
        return NET_CACHE_TIME;
    }

    /**
     * 设置是否打印LOG
     *
     * @param isDebug 开关
     */
    public static void setIsDebug(boolean isDebug) {
        IS_DEBUG = isDebug;
    }
}

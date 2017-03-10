package com.dzs.projectframe.base;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.dzs.projectframe.utils.SharedPreferUtils;
import com.dzs.projectframe.utils.SystemUtils;

/**
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/8/29.
 */
public class ProjectContext extends Application {
    public static ProjectContext appContext;
    public static Resources resources;
    public static SharedPreferUtils sharedPreferUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        resources = appContext.getResources();
        sharedPreferUtils = SharedPreferUtils.getInstanse(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        resources = null;
        sharedPreferUtils.close();
    }

    /**
     * 获取当前应用程序版本号
     *
     * @return int
     */
    public int getApplicationVersion() {
        PackageInfo info = SystemUtils.getPackageInfo(this);
        return info.versionCode;
    }

    /**
     * 获取应用程序版本名称
     *
     * @return String
     */
    public String getApplicationVersionName() {
        PackageInfo info = SystemUtils.getPackageInfo(this);
        return info.versionName;
    }


}

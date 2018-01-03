package com.dzs.projectframe.base;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import com.dzs.projectframe.base.Bean.LibEntity;
import com.dzs.projectframe.broadcast.Receiver;
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
    private Receiver receiver;
    public boolean isH5Refresh = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        resources = appContext.getResources();
        sharedPreferUtils = SharedPreferUtils.getInstanse(this);
        setBroadcast();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        resources = null;
        receiver.clear();
        unregisterReceiver(receiver);
        sharedPreferUtils.close();
    }

    /**
     * 设置广播
     */
    private void setBroadcast() {
        receiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Receiver.ACTION);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * 发送广播
     *
     * @param libEntity libEntity
     */
    public void sendBroadcast(LibEntity libEntity) {
        Intent intent = new Intent(Receiver.ACTION);
        intent.putExtra(LibEntity.class.getName(), libEntity);
        sendBroadcast(intent);
    }

    /*添加广播监听*/
    public void addReceiver(Receiver.OnBroadcastReceiverListener receiverListener) {
        receiver.addReceiver(receiverListener);
    }

    /*移除广播监听*/
    public void removeReceiver(Receiver.OnBroadcastReceiverListener receiverListener) {
        receiver.removeReceiver(receiverListener);
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

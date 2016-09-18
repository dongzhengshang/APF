package com.dzs.marketing.control;

import android.app.Activity;
import android.content.Intent;

import com.dzs.marketing.ui.LoginActivity;

/**
 * 用户
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2016/9/18.
 */
public class UserControl {
    public static UserControl userControl;

    private UserControl() {
    }

    /**
     * 单利模式
     *
     * @return datacontrol
     */
    public static UserControl getInstance() {
        if (userControl == null) {
            userControl = new UserControl();
        }
        return userControl;
    }

    /**
     * 判断是否登录
     */
    public boolean getLoginState() {
        return false;
    }

    /**
     * 判断是否登录,未登录状态下跳转到登录界面
     *
     * @param activity 当前Activity
     * @return boolean
     */
    public boolean checkIsLogin(Activity activity) {
        if (!getLoginState()) activity.startActivity(new Intent(activity, LoginActivity.class));
        return getLoginState();
    }

    public void login(String userName, String passward) {
        
    }

    public void logout() {

    }

}

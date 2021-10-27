package com.dzs.projectframe.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Stack;

/**
 * 当前应用程序Activity管理类
 *
 * @author DZS dzsk@outlook.com
 * @version 1.1
 * @date 2015-1-24 下午4:12:13
 */
public class ActivityUtils {

    private static Stack<Activity> activityStack;
    private static ActivityUtils activityUtils;

    private ActivityUtils() {
    }

    /**
     * 单利模式
     */
    public static ActivityUtils getInstanse() {
        if (activityUtils == null) {
            synchronized (ActivityUtils.class) {
                if (activityUtils == null) {
                    activityUtils = new ActivityUtils();
                }
            }
        }
        if (activityStack == null){
            synchronized (ActivityUtils.class) {
                if (activityStack == null) {
                    activityStack =new Stack<>();
                }
            }
        }
        return activityUtils;
    }

    /**
     * 添加Activity
     *
     * @param activity activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) activityStack = new Stack<>();
        else activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     *
     * @return Activity
     */
    public Activity currentActivity() {
        return activityStack != null?activityStack.lastElement():null;
    }

    /**
     * 获取指定的Activity
     *
     * @param cls class
     * @return Activity
     */
    public Activity getActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     *
     * @param activity activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param class1 class
     */
    public void finishActivity(Class<?> class1) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (((activityStack.get(i)).getClass()).equals(class1)) {
                finishActivity(activityStack.get(i));
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束除参数外的所有Activity
     *
     * @param class1 class数组
     */
    public void finishAllOtherActivity(Class<?>... class1) {
        ArrayList<Activity> activities = new ArrayList<>();
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity = activityStack.get(i);
            for (Class<?> c : class1) {
                if (activity.getClass().getName().equals(c.getName())) {
                    activityStack.remove(i);
                    i--;
                    activities.add(activity);
                    break;
                }
            }
        }
        finishAllActivity();
        activityStack.addAll(activities);

    }

    /**
     * 退出应用
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            System.gc();
        } catch (Exception e) {
            LogUtils.exception(e);
        }
    }
}

package com.dzs.projectframe.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * 堆栈工具类
 *
 * @author DZS dzsdevelop@163.com
 * @version 1.1
 * @date 2015-1-24 下午4:12:13
 */
public class StackUtils {

    private static Stack<Activity> activityStack;
    private static StackUtils stackUtils;

    private StackUtils() {
    }

    /**
     * 单利模式
     *
     * @return
     */
    public static StackUtils getInstanse() {
        if (stackUtils == null) {
            synchronized (StackUtils.class) {
                if (stackUtils == null) {
                    stackUtils = new StackUtils();
                }
            }
        }
        return stackUtils;
    }

    /**
     * 添加Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) activityStack = new Stack<>();
        else activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     *
     * @return
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 获取指定的Activity
     *
     * @param cls
     * @return
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
     * @param activity
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
     * @param class1
     */
    public void finishActivity(Class<?> class1) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(class1)) {
                finishActivity(activity);
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
     * 退出应用
     */
    public void AppExit() {
        try {
            finishAllActivity();
            // 杀死应用进程
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

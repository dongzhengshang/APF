package com.dzs.projectframe.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.dzs.projectframe.base.ProjectContext;

import java.lang.reflect.Field;

/**
 * 屏幕显示工具类、包括dp-px转换、屏幕截图
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-23 上午10:22:46
 */
public class ScreenUtils {

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp
     * @return int
     */
    public static int dip2px(Context context, float dpValue) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, r.getDisplayMetrics());
        return (int) px;
    }

    /**
     * dp转px
     *
     * @param dpValue dp
     * @return int
     */
    public static int dip2px( float dpValue) {
        Resources r = ProjectContext.appContext.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, r.getDisplayMetrics());
        return (int) px;
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px
     * @return int
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px
     * @return int
     */
    public static int px2sp(Context context, float pxValue) {
        float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp
     * @return int
     */
    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp
     * @return int
     */
    public static int sp2px( float spValue) {
        float fontScale = ProjectContext.appContext.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 获取屏幕宽度
     *
     * @param aty 当前Activity
     * @return int
     */
    public static int getScreenW(Context aty) {
        return aty.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param aty 上下文
     * @return int
     */
    public static int getScreenH(Context aty) {
        return aty.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取状态栏高度
     *
     * @param aty 上下文
     * @return int
     */
    public static int getStatusBarHeight(Context aty) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = aty.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 获取ActionBar的高度
     *
     * @param aty 上下文
     * @return int
     */
    public int getActionBarHeight(Context aty) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (aty.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))// 如果资源是存在的、有效的
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, aty.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * 截屏
     *
     * @param v view
     * @return bitmap
     */
    public static Bitmap captureView(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return v.getDrawingCache();
    }

    /**
     * 测量控件宽度
     *
     * @param view view
     * @return int
     */
    public static int getViewWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }

    /**
     * 测量控件高度
     *
     * @param view view
     * @return int
     */
    public static int getViewHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    /**
     * 计算ListView总高度
     *
     * @param listView
     * @return
     */
    public static int getListViewTotalHieght(ListView listView) {
        int totalHeight = 0;

        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return 0;
        }
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        return totalHeight;
    }

}

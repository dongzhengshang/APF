package com.dzs.projectframe.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.dzs.projectframe.adapter.ViewHolder;

/**
 * CustomPopupWindow
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.1
 * @date 2016/3/14
 */
public class CustomPopupWindow extends PopupWindow {
    protected ViewHolder vu;

    public CustomPopupWindow(Context context, int resuaseId) {
        creadPopupwindow(context, resuaseId, 0, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CustomPopupWindow(Context context, int resuaseId, int animal) {
        creadPopupwindow(context, resuaseId, animal, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public CustomPopupWindow(Context context, int resuaseId, int animal, int w, int h) {
        creadPopupwindow(context, resuaseId, animal, w, h);
    }

    private void creadPopupwindow(Context context, int resuaseId, int animal, int w, int h) {
        vu = ViewHolder.get(context, resuaseId);
        setWidth(w);
        setHeight(h);
        //防止被虚拟按键挡住
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new ColorDrawable(0));
        if (animal != 0) {
            setAnimationStyle(animal);
        }
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(vu.getView());
        update();
    }

    public ViewHolder getVu() {
        return vu;
    }
}

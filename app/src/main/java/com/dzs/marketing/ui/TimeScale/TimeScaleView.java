package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * 时间刻度尺
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/6/2
 */
public class TimeScaleView extends RelativeLayout {
    private Scale scale;
    private AnimView animView;
    private Animation animation;

    public TimeScaleView(Context context) {
        super(context);
        init(context);
    }

    public TimeScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /*初始化*/
    private void init(Context context) {
        ScalePoint scalePoint = new ScalePoint(context);
        scale = new Scale(context);
        animView = new AnimView(context);
        this.addView(scale);
        this.addView(scalePoint);
        this.addView(animView);
        animView.setVisibility(INVISIBLE);
    }

    /*获取时间刻度尺子*/
    public Scale getScale() {
        return scale;
    }

    /*创建动画*/
    private void creatAnim() {
        animation = new TranslateAnimation(-100, getWidth(), 0, 0);
        animation.setDuration(1000);
        animation.setRepeatCount(1000);
        animation.setFillAfter(true);
    }

    /*开启动画*/
    public void showAnim() {
        animView.setVisibility(VISIBLE);
        if (animation == null) creatAnim();
        animView.startAnimation(animation);
    }

    /*关闭动画*/
    public void closeAnim() {
        animView.clearAnimation();
        animView.setVisibility(INVISIBLE);
    }
}

package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.util.AttributeSet;
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
        this.addView(scale);
        this.addView(scalePoint);
    }

    /*获取时间刻度尺子*/
    public Scale getScale() {
        return scale;
    }

    /*开启动画*/
    public void showAnim() {

    }

    /*关闭动画*/
    public void closeAnim() {

    }
}

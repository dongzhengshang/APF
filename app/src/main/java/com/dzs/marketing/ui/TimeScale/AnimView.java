package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 动画
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/6/1
 */
public class AnimView extends View {
    private Paint paint;//画笔
    private String startColor = "#66dd2a1d";
    private String endColor = "#00000000";
    private int layoutWight = 0;

    public AnimView(Context context) {
        super(context);
        init(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /*初始化*/
    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas, getWidth(), getHeight());
    }

    /*绘制布局*/
    private void drawView(Canvas canvas, int wight, int height) {
        LinearGradient linearGradient = new LinearGradient(wight / 4, 0, 0, 0, Color.parseColor(startColor), Color.parseColor(endColor), Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
        canvas.drawRect(0, 0, wight / 4, height, paint);
        this.layoutWight = wight / 4;
    }

    /*设置颜色*/
    public void setColor(String start, String end) {
        this.startColor = start;
        this.endColor = end;
    }

    /*获取渐变宽度*/
    public int getLayoutWight() {
        return layoutWight;
    }
}

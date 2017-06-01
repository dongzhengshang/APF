package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

/**
 * 刻度指针
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/6/1
 */
public class ScalePoint extends View {
    private int wight;//控件宽度
    private int height;//控件高度
    private int color = Color.RED;//默认为红色
    private Paint paint = new Paint();//画笔
    private Path pathA = new Path();//上边三角形
    private Path pathB = new Path();//下边三角形

    public ScalePoint(Context context) {
        super(context);
        init(context);
    }

    public ScalePoint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScalePoint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /*初始化*/
    private void init(Context context) {
        wight = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        //上边的三角形
        pathA.moveTo(0, 0);
        pathA.lineTo(wight, 0);
        pathA.lineTo(wight / 2, wight / 2);
        pathA.close();
        canvas.drawPath(pathA, paint);
        //下边三角形
        pathB.moveTo(0, height);
        pathB.lineTo(wight, height);
        pathB.lineTo(wight / 2, wight / 2);
        pathB.close();
        canvas.drawPath(pathB, paint);
        //绘制指针
        canvas.drawLine(wight / 2, 0, wight / 2, height, paint);
    }

    /*设置控件宽度*/
    public void setWight(int wight) {
        this.wight = wight;
        invalidate();
    }

    /*设置控件高度*/
    public void setHeight(int height) {
        this.height = height;
        invalidate();
    }

    /*设置控件颜色*/
    public void setColor(int color) {
        this.color = color;
        invalidate();
    }
}

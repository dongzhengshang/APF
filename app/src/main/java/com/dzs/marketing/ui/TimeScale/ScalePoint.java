package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 刻度指针
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/6/1
 */
public class ScalePoint extends View {
    private Paint paint;//画笔
    private int triangleW = 20;//默认三角形底边宽度为20

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
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTriangle(canvas, getWidth(), getHeight());
    }

    /*绘制指针*/
    private void drawTriangle(Canvas canvas, int wight, int height) {
        //上边的三角形
        Path pathA = new Path();
        pathA.moveTo(wight / 2 - triangleW, 0);
        pathA.lineTo(wight / 2 + triangleW, 0);
        pathA.lineTo(wight / 2, triangleW / 2);
        pathA.close();
        canvas.drawPath(pathA, paint);
        //下边三角形
        Path pathB = new Path();
        pathB.moveTo(wight / 2 - triangleW, height);
        pathB.lineTo(wight / 2 + triangleW, height);
        pathB.lineTo(wight / 2, height - triangleW / 2);
        pathB.close();
        canvas.drawPath(pathB, paint);
        //绘制指针
        canvas.drawLine(wight / 2, 0, wight / 2, height, paint);
    }

    /*设置画笔*/
    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    /*设置三角形底边宽度*/
    public void setTriangleW(int triangleW) {
        this.triangleW = triangleW;
    }
}

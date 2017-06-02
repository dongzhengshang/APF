package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 刻度指针
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/6/1
 */
public class AnimView extends View {
    private Paint paint;//画笔
    private LinearGradient linearGradient;
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas, getWidth(), getHeight());
    }

    /*绘制指针*/
    private void drawView(Canvas canvas, int wight, int height) {
//        linearGradient = new LinearGradient();
//        paint.setShader(linearGradient);
//        canvas.drawRect(0, 0, (wight - 40), 0, paint);

    }

    /*设置画笔*/
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}

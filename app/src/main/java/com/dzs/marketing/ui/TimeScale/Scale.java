package com.dzs.marketing.ui.TimeScale;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

import androidx.annotation.Nullable;

import com.dzs.projectframe.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 时间刻度
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/6/1
 */
public class Scale extends View {
    private int dis = 20;//刻度间距，默认为20像素
    private int shortScaleHeight = 30;//短刻度线条高度
    private int longScaleHeight = (int) (1.5 * shortScaleHeight);//长刻度线条高度
    private int lineWidth = 4;//刻度线条宽度
    private int textSiz = 35;//字体大小
    private Paint scalePaint = new Paint();//刻度画笔
    private Paint videoPaint = new Paint();//视频片段
    private List<TimePart> list;
    private int currentMinute;//当前分钟数
    private OverScroller scroller;
    private float lastX = 0;
    private Point rightPoint = new Point();//最左边坐标点
    private Point leftPoint = new Point();//最右边坐标点
    private VelocityTracker vTracker = null;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private int bgColor = Color.WHITE;

    public Scale(Context context) {
        super(context);
        init(context);
    }

    public Scale(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Scale(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas, getWidth(), getHeight());
        drawTimeRect(canvas, getWidth(), getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        obtainVelocityTracker(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (scroller != null && !scroller.isFinished()) scroller.abortAnimation();
                lastX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float dataX = -(endX - lastX);
                if ((scroller.getFinalX() + dataX < leftPoint.x - getWidth() / 2) || (scroller.getFinalX() + dataX > rightPoint.x - getWidth() / 2)) dataX = 0;
                scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), (int) dataX, 0);
                lastX = event.getX();
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                vTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) vTracker.getXVelocity();
                if (Math.abs(initialVelocity) > mMinimumVelocity) {
                    scroller.fling(scroller.getFinalX(), scroller.getFinalY(), -initialVelocity, 0, leftPoint.x - getWidth() / 2, rightPoint.x - getWidth() / 2, 0, 0);
                }
                invalidate();
                releaseVelocityTracker();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        if (scroller.isFinished()) {
            LogUtils.debug(scroller.getFinalX());
        }
    }

    /*初始化*/
    private void init(Context context) {
        scalePaint.setAntiAlias(true);//抗锯齿
        videoPaint.setAntiAlias(true);//抗锯齿
        scalePaint.setStrokeWidth(lineWidth);//线条宽度
        scalePaint.setColor(Color.GRAY);
        scalePaint.setTextSize(textSiz);
        videoPaint.setStrokeWidth(lineWidth);//线条宽度
        videoPaint.setColor(Color.parseColor("#6B00FFFF"));
        list = new ArrayList<>();
        currentMinute = getTodayStartMinute();
        scroller = new OverScroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    /*VelocityTracker初始化*/
    private void obtainVelocityTracker(MotionEvent event) {
        if (vTracker == null) {
            vTracker = VelocityTracker.obtain();
        }
        vTracker.addMovement(event);
    }

    /*VelocityTracker释放*/
    private void releaseVelocityTracker() {
        if (vTracker != null) {
            vTracker.clear();
            vTracker.recycle();
            vTracker = null;
        }
    }

    /*绘制时间刻度，从中间往两边开始绘制*/
    private void drawScale(Canvas canvas, int wight, int height) {
        canvas.drawColor(bgColor);
        //右边刻度线
        for (int i = currentMinute; i <= 1440; i++) {
            if (i == 1440) rightPoint.set((wight / 2) + (i - currentMinute) * dis, 0);
            if (i % 5 == 0) {
                canvas.drawLine((wight / 2) + (i - currentMinute) * dis, 0, (wight / 2) + (i - currentMinute) * dis, longScaleHeight, scalePaint);
                canvas.drawLine((wight / 2) + (i - currentMinute) * dis, height - longScaleHeight, (wight / 2) + (i - currentMinute) * dis, height, scalePaint);
                if (i % 6 == 0) {
                    String text = formatString(i);
                    canvas.drawText(text, ((wight / 2) + (i - currentMinute) * dis) - scalePaint.measureText(text) / 2, (float) (longScaleHeight * 2), scalePaint);
                }
            } else {
                canvas.drawLine((wight / 2) + (i - currentMinute) * dis, 0, (wight / 2) + (i - currentMinute) * dis, shortScaleHeight, scalePaint);
                canvas.drawLine((wight / 2) + (i - currentMinute) * dis, height - shortScaleHeight, (wight / 2) + (i - currentMinute) * dis, height, scalePaint);
            }
        }
        //左边刻度线
        for (int j = currentMinute - 1; j >= 0; j--) {
            if (j == 0) leftPoint.set((wight / 2) + (j - currentMinute) * dis, 0);
            if (j % 5 == 0) {
                canvas.drawLine((wight / 2) + (j - currentMinute) * dis, 0, (wight / 2) + (j - currentMinute) * dis, longScaleHeight, scalePaint);
                canvas.drawLine((wight / 2) + (j - currentMinute) * dis, height - longScaleHeight, (wight / 2) + (j - currentMinute) * dis, height, scalePaint);
                if (j % 6 == 0) {
                    String text = formatString(j);
                    canvas.drawText(text, ((wight / 2) + (j - currentMinute) * dis) - scalePaint.measureText(text) / 2, (float) (longScaleHeight * 2), scalePaint);
                }
            } else {
                canvas.drawLine((wight / 2) + (j - currentMinute) * dis, 0, (wight / 2) + (j - currentMinute) * dis, shortScaleHeight, scalePaint);
                canvas.drawLine((wight / 2) + (j - currentMinute) * dis, height - shortScaleHeight, (wight / 2) + (j - currentMinute) * dis, height, scalePaint);
            }
        }
        canvas.drawLine(leftPoint.x, 0, rightPoint.x, 0, scalePaint);
        canvas.drawLine(leftPoint.x, height, rightPoint.x, height, scalePaint);
    }

    /*绘制时间片段*/
    private void drawTimeRect(Canvas canvas, int width, int height) {
        for (TimePart timePart : list) {
            canvas.drawRect(getTimeP(timePart.getsHour(), timePart.getsMinute()).x, 0, getTimeP(timePart.geteHour(), timePart.geteMinute()).x, height, videoPaint);
        }
    }

    /*获取今天已经经过的分钟数*/
    private int getTodayStartMinute() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long l = System.currentTimeMillis() - cal.getTimeInMillis();
        LogUtils.debug(l);
        return (int) ((l / 1000) / 60);
    }

    /*格式化时间*/
    private String formatString(int i) {
        int h = i / 60;
        return h >= 10 ? h + (i % 60 == 0 ? ":00" : ":30") : "0" + h + (i % 60 == 0 ? ":00" : ":30");
    }

    /*时间片段*/
    public static class TimePart {
        //开始的时间和结束时间
        private int sHour, sMinute, eHour, eMinute;

        public TimePart(int sHour, int sMinute, int eHour, int eMinute) {
            this.sHour = sHour;
            this.sMinute = sMinute;
            this.eHour = eHour;
            this.eMinute = eMinute;
        }

        public int getsHour() {
            return sHour;
        }

        public int getsMinute() {
            return sMinute;
        }

        public int geteHour() {
            return eHour;
        }

        public int geteMinute() {
            return eMinute;
        }
    }

    /*获取时间坐标*/
    public Point getTimeP(int sHour, int sMinute) {
        Point point = new Point();
        int startMin = 60 * sHour + sMinute;
        point.set(leftPoint.x + (startMin * dis), 0);
        return point;
    }

    /*设置视频*/
    public void setRect(List<TimePart> list) {
        if (list != null && !list.isEmpty()) this.list.addAll(list);
        invalidate();
    }

    /*清理视频*/
    public void clearRect() {
        list.clear();
        invalidate();
    }

    /*设置刻度间隔*/
    public void setDis(int dis) {
        this.dis = dis;
    }

    /*设置短线条长度*/
    public void setShortScaleHeight(int shortScaleHeight) {
        this.shortScaleHeight = shortScaleHeight;
    }

    /*设置刻度画笔*/
    public void setScalePaint(Paint scalePaint) {
        this.scalePaint = scalePaint;
    }

    /*设置视频片段画笔*/
    public void setVideoPaint(Paint videoPaint) {
        this.videoPaint = videoPaint;
    }

    /*设置背景*/
    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}

package com.dzs.marketing.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Scroller;

import com.dzs.projectframe.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 时间轴
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2017/5/31
 */
public class TimeScale extends View {
    private Context context;
    private int dis = 20;//刻度间距，默认为20像素
    private int shortScaleHeight = 30;//短刻度线条高度
    private int longScaleHeight = (int) (1.5 * shortScaleHeight);//长刻度线条高度
    private int lineWidth = 4;//刻度线条宽度
    private int textSiz = 35;//字体大小
    private Paint scalePaint = new Paint();//刻度画笔
    private Paint linePaint = new Paint();//刻度画笔
    private List<TimePart> list;
    private int currentMinute;
    private Scroller scroller;
    private float lastX = 0;


    public TimeScale(Context context) {
        super(context);
        init(context);
    }

    public TimeScale(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeScale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawScale(canvas, getWidth(), getHeight());
        drawTriangle(canvas, getWidth(), getHeight());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (scroller != null && !scroller.isFinished()) scroller.abortAnimation();
                lastX = x;
                return true;
            case MotionEvent.ACTION_MOVE:
                float dataX = lastX - x;
                int finalX = scroller.getFinalX();
//                //右边
//                if (dataX < 0) {
//                    if (finalX < -viewWidth / 2) {
//                        return super.onTouchEvent(event);
//                    }
//                }
//                if (dataX > 0) {
//                    if (finalX > timeScale * 21) {
//                        return super.onTouchEvent(event);
//                    }
//                }
                scroller.startScroll(scroller.getFinalX(), scroller.getFinalY(), (int) dataX, 0);
                lastX = x;
                postInvalidate();
                return true;
//            case MotionEvent.ACTION_UP:
//                int finalx1 = scroller.getFinalX();
//                if (finalx1 < -viewWidth / 2) {
//                    scroller.setFinalX(-viewWidth / 2);
//                }
//                if (finalx1 > timeScale * 21) {
//                    scroller.setFinalX(timeScale * 21);
//                }
//                if (scrollListener != null) {
//                    int finalX = scroller.getFinalX();
//                    //表示每一个屏幕刻度的一半的总秒数，每一个屏幕有6格
//                    int sec = 3 * 3600;
//                    //滚动的秒数
//                    int temsec = (int) Math.rint((double) finalX / (double) timeScale * 3600);
//                    sec += temsec;
//                    //获取的时分秒
//                    int thour = sec / 3600;
//                    int tmin = (sec - thour * 3600) / 60;
//                    int tsec = sec - thour * 3600 - tmin * 60;
//                    scrollListener.onScrollFinish(thour, tmin, tsec);
//                }
//                postInvalidate();
//                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    /*初始化*/
    private void init(Context context) {
        this.context = context;
        scalePaint.setAntiAlias(true);//抗锯齿
        scalePaint.setStrokeWidth(lineWidth);//线条宽度
        scalePaint.setColor(Color.GRAY);
        scalePaint.setTextSize(textSiz);
        linePaint.setAntiAlias(true);//抗锯齿
        linePaint.setStrokeWidth(lineWidth);//线条宽度
        linePaint.setColor(Color.RED);
        list = new ArrayList<>();
        currentMinute = getTodayStartMinute();
        scroller = new Scroller(context);
    }

    /*绘制指针*/
    private void drawTriangle(Canvas canvas, int wight, int height) {
        //上边的三角形
        Path pathA = new Path();
        pathA.moveTo(wight / 2 - dis, 0);
        pathA.lineTo(wight / 2 + dis, 0);
        pathA.lineTo(wight / 2, dis / 2);
        pathA.close();
        canvas.drawPath(pathA, linePaint);
        //下边三角形
        Path pathB = new Path();
        pathB.moveTo(wight / 2 - dis, height);
        pathB.lineTo(wight / 2 + dis, height);
        pathB.lineTo(wight / 2, height - dis / 2);
        pathB.close();
        canvas.drawPath(pathB, linePaint);
        //绘制指针
        canvas.drawLine(wight / 2, 0, wight / 2, height, linePaint);
    }

    /*绘制时间刻度，从中间往两边开始绘制*/
    private void drawScale(Canvas canvas, int wight, int height) {
        canvas.drawColor(Color.WHITE);
        //右边刻度线
        for (int i = currentMinute; i <= 1440; i++) {
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
        canvas.drawLine(0, 0, wight, 0, scalePaint);
        canvas.drawLine(0, height, wight, height, scalePaint);
    }

    /*绘制时间片段*/
    private void drawTimeRect(Canvas canvas, int wight, int height) {
        for (TimePart timePart : list) {
            //canvas.drawRect();
        }
        postInvalidate();
    }

    /*绘制动画*/
    private void drawAnimal(Canvas canvas, int wight, int height) {

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
    public class TimePart {
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

        public void setsHour(int sHour) {
            this.sHour = sHour;
        }

        public int getsMinute() {
            return sMinute;
        }

        public void setsMinute(int sMinute) {
            this.sMinute = sMinute;
        }

        public int geteHour() {
            return eHour;
        }

        public void seteHour(int eHour) {
            this.eHour = eHour;
        }

        public int geteMinute() {
            return eMinute;
        }

        public void seteMinute(int eMinute) {
            this.eMinute = eMinute;
        }
    }
}

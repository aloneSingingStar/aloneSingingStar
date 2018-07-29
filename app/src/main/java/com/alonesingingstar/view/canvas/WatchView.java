package com.alonesingingstar.view.canvas;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.alonesingingstar.utils.Tool;

import java.util.Calendar;

public class WatchView extends View{
    private Paint paint;
    private Calendar calendar;
    private float cx;
    private float cy;

    public WatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        calendar = Calendar.getInstance();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.max(Tool.dp2px(300),getSuggestedMinimumWidth());
        int height = Math.max(Tool.dp2px(300),getSuggestedMinimumHeight());
        setMeasuredDimension(resolveSizeAndState(width,widthMeasureSpec,0),
                resolveSizeAndState(height,heightMeasureSpec,0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float radius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        cx = getPaddingLeft() + radius;
        cy = getPaddingTop() + radius;
        paint.setColor(Color.RED);//如果在构造中设置，圆也变成BLACK
        canvas.drawCircle(cx,cy, radius,paint);
        canvas.save();//保存当前画布状态

        drawQuarter(canvas);
        drawPointer(canvas);
        drawNumber(canvas);

    }

    private void drawNumber(Canvas canvas) {
        canvas.restore();//恢复上一次保存的状态，即从12点钟开始绘制
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        canvas.drawText("12",cx,getPaddingTop()+Tool.dp2px(30)+Math.abs(paint.getFontMetrics().top),paint);
        canvas.rotate(30,cx,cy);
        for (int i=1;i<12;i++){
            paint.setStrokeWidth(4);
            paint.setColor(Color.BLACK);
            canvas.drawText(String.valueOf(i),cx,getPaddingTop()+Tool.dp2px(30)+Math.abs(paint.getFontMetrics().top),paint);
            canvas.rotate(30,cx,cy);
        }
    }

    /**
     * 已知圆的中心点坐标(x0,y0),半径r,则圆上任何角度@对应的坐标公式为
     * x=x0+r*cos@
     * y=y0+r*sin@
     * cos@对应Math.cos(),但是Math.cos()的参数是弧度，所以需要使用Math.toRadians()转换成弧度
     * 此处设置时针的长度是0.5r,分针的长度是0.6r
     * @param canvas 画布
     */
    private void drawPointer(Canvas canvas) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hours = calendar.get(Calendar.HOUR)%12;
        int minutes = calendar.get(Calendar.MINUTE);

        paint.setStrokeWidth(15);
        canvas.rotate(-90,cx,cy);//0度是从3点处开始的，但是时间角度是从12点开始计算的，所以需要以原点为中心旋转-90.
        float stopX = (float) (cx + cx*0.5*Math.cos(Math.toRadians(30*hours)));
        float stopY = (float) (cy + cy*0.5*Math.sin(Math.toRadians(30*hours)));
        canvas.drawLine(cx,cy,stopX,stopY,paint);

        paint.setStrokeWidth(5);
        stopX = (float) (cx + cx*0.6*Math.cos(Math.toRadians(5*minutes)));
        stopY = (float) (cy + cy*0.6*Math.sin(Math.toRadians(5*minutes)));
        canvas.drawLine(cx,cy,stopX,stopY,paint);
    }

    private void drawQuarter(Canvas canvas) {
        int length = 0;
        for (int i=0;i<60;i++){
            if (i%5 == 0){
                paint.setStrokeWidth(4);
                paint.setColor(Color.BLACK);
                length = Tool.dp2px(30);
            }else {
                paint.setStrokeWidth(1);
                paint.setColor(Color.BLUE);
                length = Tool.dp2px(15);
            }
            canvas.drawLine(cx,getPaddingTop(),cx,getPaddingTop()+length,paint);
            canvas.rotate(6,cx,cy);
        }
    }
}

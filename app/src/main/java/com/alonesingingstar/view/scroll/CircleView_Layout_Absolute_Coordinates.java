package com.alonesingingstar.view.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.alonesingingstar.R;
import com.alonesingingstar.utils.Tool;

public class CircleView_Layout_Absolute_Coordinates extends View{
    private static final String TAG = "CircleView";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int color;
    private float lastRawX;
    private float lastRawY;

    /**
     *
     * @param context
     * @param attrs 布局文件中定义的属性，比如android:layout_width
     */
    public CircleView_Layout_Absolute_Coordinates(Context context, @Nullable AttributeSet attrs) {
        //最终表示AttributeSet=attrs,defStyleAttr=0,defStyleRes=0，如果这里attrs传入null,那么布局文件中设置的属性就不起作用
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        color = array.getColor(R.styleable.CircleView_circle_color, context.getResources().getColor(R.color.colorAccent));
        array.recycle();
    }

    /**
     *
     * @param widthMeasureSpec CircleView的width测量规格，由父容器widthMeasureSpec+自身LayoutParams计算出
     * @param heightMeasureSpec CircleView的height测量规格，由父容器widthMeasureSpec+自身LayoutParams计算出
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //支持wrap_content、padding,最小支持的width在300dp、背景图片高度+自身padding、android:minWidth+自身padding中取最大值
        int width = Math.max(Tool.dp2px(300),getPaddingLeft()+getPaddingRight()+getSuggestedMinimumWidth());
        int height = Math.max(Tool.dp2px(300),getPaddingTop()+getPaddingBottom()+getSuggestedMinimumHeight());
        setMeasuredDimension(resolveSizeAndState(width,widthMeasureSpec,0),resolveSizeAndState(height,heightMeasureSpec,0));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cx = getPaddingLeft()+(getWidth()-getPaddingLeft()-getPaddingRight())/2;
        float cy = getPaddingTop()+(getHeight()-getPaddingTop()-getPaddingBottom())/2;
        float radius = Math.min((getWidth()-getPaddingLeft()-getPaddingRight())/2,
                (getHeight()-getPaddingTop()-getPaddingBottom())/2);
        paint.setColor(color);
        canvas.drawCircle(cx,cy,radius,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastRawX = rawX;
                lastRawY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                int offSetX = (int) (rawX - lastRawX);
                int offsetY = (int) (rawY - lastRawY);
                layout(getLeft()+offSetX,
                        getTop()+offsetY,
                        getRight()+offSetX,
                        getBottom()+offsetY);
                lastRawX = rawX;
                lastRawY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                performClick();
                break;
            default:
                break;
        }
        return true;
    }
}

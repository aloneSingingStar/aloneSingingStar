package com.alonesingingstar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.alonesingingstar.R;
import com.alonesingingstar.utils.Tool;

/**
 * 一、构造函数的调用时机
 * 1.直接在代码中new CircleView(),调用CircleView(Context context)
 * 2.其他情况，都是调用CircleView(Context context, @Nullable AttributeSet attrs)
 * 3.剩下的两个构造都不会主动调用，如果有需要，需手动调用
 *
 * 二、自定义属性赋值优先级
 * 布局文件定义
 * >style="@style/testStyle"
 * >自定义View所在Activity的Theme中指定的style
 * >构造函数中defStyleRes指定的默认值
 * 具体参考：https://blog.csdn.net/wzy_1988/article/details/49619773
 *
 * 三、CircleView的绘制时的原点是CircleView的左上角，并不是以父容器的左上角为参考
 */
public class CircleView extends View{
    private static final String TAG = "CircleView";
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int color;

    /**
     *
     * @param context
     * @param attrs 布局文件中定义的属性，比如android:layout_width
     */
    public CircleView(Context context, @Nullable AttributeSet attrs) {
        //最终表示AttributeSet=attrs,defStyleAttr=0,defStyleRes=0，如果这里attrs传入null,那么布局文件中设置的属性就不起作用
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CircleView);
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
}

package com.example.caosir.jibu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * 创建人: caosir
 * 创建时间：2017/12/13
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class MyView extends android.support.v7.widget.AppCompatImageView
{


    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("测量过程","子view 测量开始测量---myview 传入的宽===="+MeasureSpec.getSize(widthMeasureSpec));
        Log.d("测量过程","子view 开始测量 子view"+MeasureSpec.getSize(widthMeasureSpec));
        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.AT_MOST:
                Log.d("测量过程","子view 开始测量 传入的模式是ATMOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.d("测量过程","子view 开始测量 传入的模式是Exactly 精准模式");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d("测量过程","子view 开始测量 传入的模式是未指定模式");
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("测量过程","子view 测量完成---myview 算出的宽====="+getMeasuredWidth());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("MyView","onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("MyView","onDraw");
    }

    @Override
    public void invalidate() {
        super.invalidate();
        Log.d("MyView","invalidate");
    }

    @Override
    public void setX(float x) {
        super.setX(x) ;
        Log.d("MyView","setX"+x);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("事件分发","MyView");
        return super.dispatchTouchEvent(event);
    }
}

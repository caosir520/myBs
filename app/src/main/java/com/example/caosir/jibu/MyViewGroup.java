package com.example.caosir.jibu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 创建人: caosir
 * 创建时间：2017/12/13
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class MyViewGroup extends android.support.constraint.ConstraintLayout {
    public MyViewGroup(Context context) {
        this(context,null);
    }

    public MyViewGroup(Context context,  AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyViewGroup(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("事件分发","ViewGroup");
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("测量过程","父view 遍历子view 宽size"+MeasureSpec.getSize(widthMeasureSpec));

        super.measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        Log.d("测量过程","父view 开始测量子view"+MeasureSpec.getSize(parentWidthMeasureSpec));

        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d("测量过程","父view 开始测量 子view"+MeasureSpec.UNSPECIFIED);
        Log.d("测量过程","父view 开始测量 子view"+MeasureSpec.getSize(widthMeasureSpec));


        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.AT_MOST:
                Log.d("测量过程","父view 开始测量 传入的模式是ATMOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.d("测量过程","父view 开始测量 传入的模式是Exactly 精准模式");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d("测量过程","父view 开始测量 传入的模式是未指定模式");
                break;
        }
        //查看一个子View的LayoutParams
        View view = getChildAt(0);
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        Log.d("测量过程","子View的layoutPrams"+lp.width);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("测量过程","父view 测量完成 子view"+MeasureSpec.getSize(widthMeasureSpec));
    }
  /*  @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
         super.onInterceptTouchEvent(ev);
         return true;

    }*/
}

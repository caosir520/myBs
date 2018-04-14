package com.example.caosir.jibu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 创建人: caosir
 * 创建时间：2018/3/6
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class CircleProssView extends View {
    //画笔
    Paint circlePaint, textPaint;

    int screenW, screenH;

    private int now;
    private float defaultMax=10000;
    public String danwei="步";

    public int getNow() {
        return now;
    }

    public void setNow(int now) {
        this.now = now;
        Log.d("cir",""+now);
        invalidate();
    }

    public float getDefaultMax() {
        return defaultMax;
    }

    public void setDefaultMax(float defaultMax) {
        this.defaultMax = defaultMax;
    }

    public CircleProssView(Context context) {
        this(context, null);
    }

    public CircleProssView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProssView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        screenH=getMeasuredHeight();
        screenW=getMeasuredWidth();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画一个矩形
        Path dashPath=new Path();
        RectF rectF = new RectF(0,0,3,20);
        dashPath.addRect(rectF,Path.Direction.CCW);
        //设置画笔颜色
        circlePaint.setColor(Color.RED);
        PathEffect pathEffect = new PathDashPathEffect(dashPath,10,0, PathDashPathEffect.Style.MORPH);
        circlePaint.setPathEffect(pathEffect);
        circlePaint.setAlpha(100);
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(0,0,screenW,screenH,-225,270,false,circlePaint);
        circlePaint.setAlpha(225);
        float bili = now/defaultMax;
        canvas.drawArc(0,0,screenW,screenH,-225,270*bili,false,circlePaint);
        //写s
        String text = ""+now+danwei;
        //测量text长度。
        textPaint.setTextSize(70);
        float textW =  textPaint.measureText(text);
        canvas.drawText(text,(screenW-textW)/2, (float) (0.85*screenH),textPaint);

    }
}

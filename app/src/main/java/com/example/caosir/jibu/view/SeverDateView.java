package com.example.caosir.jibu.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.method.Touch;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.wise.common.commonutils.LogUtil;
import com.wise.common.commonutils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建人: caosir
 * 创建时间：2018/3/6
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class SeverDateView extends View {
    private Paint rectPaint,textPaint;

    int screenW, screenH;
    Click click;

    public Click getClick() {
        return click;
    }

    public void setClick(Click click) {
        this.click = click;
    }

    public SeverDateView(Context context) {
        this(context,null);
    }

    public SeverDateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SeverDateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        screenH=getMeasuredHeight();
        screenW=getMeasuredWidth();
        rectInterval=(screenW-7*rectW)/6;

        for (int i =0;i<date.size();i++){
            if(maxDate<date.get(i)){
                maxDate=date.get(i);
            }
        }
        maxRectH = (float) (0.8*screenH);
    }
    //柱形宽度
    float rectW=30;
    //每个rect的间隔
    float rectInterval;
    //7天数据
    List<Integer> date = new ArrayList<>();
    List<RectF> rectList = new ArrayList<>();
    //7天最大数据
    int maxDate=0;
    float chengdu=1f;
    float maxRectH ;

    public List<Integer> getDate() {
        return date;
    }

    public void setDate(List<Integer> date) {
        this.date = date;
    }

    public float getChengdu() {
        return chengdu;
    }

    public void setChengdu(float chengdu) {
        this.chengdu = chengdu;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rectPaint.setColor(Color.RED);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(rectW);
        LogUtil.d(date.toString());
        rectList= new ArrayList<>();
        for(int i =0;i<date.size();i++){
            //画矩阵
            //计算当前数据 矩阵的长度
            float dateRectH  = maxRectH*(date.get(i).floatValue()/maxDate);
            LogUtil.d("长度"+date.get(i).floatValue()/maxDate);
            canvas.drawRect(i*(rectW+rectInterval),maxRectH-dateRectH*chengdu-20,i*(rectW+rectInterval)+rectW, (float) (0.8*screenH),rectPaint);
            //记录当前矩阵的坐标位子
            RectF rectF = new RectF(i*(rectW+rectInterval),maxRectH-dateRectH-20,i*(rectW+rectInterval)+rectW, (float) (0.8*screenH));
            rectList.add(rectF);
            //写下面的字
            if(i==6){
                textPaint.setTextSize(rectW);
                canvas.drawText("今",i*(rectW+rectInterval),(float) (0.8*screenH)+50,textPaint);
            }else{
                LogUtil.d(TimeUtil.getOldDate(-7+i).substring(8));
                canvas.drawText(TimeUtil.getOldDate(-6+i).substring(8),i*(rectW+rectInterval),(float) (0.8*screenH)+50,textPaint);
            }

        }
    }






    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取x，y
        float x = event.getX();
        float y = event.getY();
        for(int i =0;i<rectList.size();i++){
            if(rectList.get(i).left<=x&&rectList.get(i).right>=x&&rectList.get(i).top<y&&rectList.get(i).bottom>y){
                click.onClick(i);
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    public interface Click{
        public void onClick(int i );

    }
}

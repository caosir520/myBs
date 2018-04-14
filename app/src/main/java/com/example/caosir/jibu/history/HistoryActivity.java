package com.example.caosir.jibu.history;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.caosir.jibu.MainActivity;
import com.example.caosir.jibu.R;
import com.example.caosir.jibu.base.BaseCoreMVPActivity;
import com.example.caosir.jibu.config.Constant;
import com.example.caosir.jibu.pojo.Exercise;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.weiget.CalendarView;
import com.wise.common.basedialog.dialogplus.OnClickListener;
import com.wise.common.basedialog.dialogplus.ViewHolder;
import com.wise.common.basedialog.dialogplus.DialogPlus;
import com.wise.common.commonutils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 创建人: caosir
 * 创建时间：2018/4/11
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class HistoryActivity extends BaseCoreMVPActivity<HistoryActivityPersent, HistoryActivityPersent.Ui> implements HistoryActivityPersent.Ui {

    @BindView(R.id.bt_more)
    Button bt_more;
    @BindView(R.id.bt_ok)
    Button bt_ok;
    @BindView(R.id.bt_time)
    Button bt_time;
    @BindView(R.id.food_xiangxi_piechar)
    PieChart pieChart;

    private String data;

    private String checkTime;
    java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = TimeUtil.getOldDate(0);
        bt_time.setText(data);
        getPresenter().getDayDate(data);
    }

    @OnClick(R.id.bt_more)
    public void setBt_more() {
        showConfirmDialog();
    }

    @OnClick(R.id.bt_ok)
    public void setBt_ok() {
        getPresenter().getDayDate(data);
    }
    @OnClick(R.id.back)
    public void setBack(){
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void showConfirmDialog() {
        ViewHolder holder = new ViewHolder(R.layout.dialog_check_time);
        DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setContentHolder(holder)
                .setGravity(Gravity.BOTTOM)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.bt_ok:
                                data = checkTime;
                                bt_time.setText(data);
                                getPresenter().getDayDate(data);
                                dialog.dismiss();
                                break;
                            case R.id.bt_quxiao:
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .create();
        View holderView = dialog.getHolderView();
        final Button time = holderView.findViewById(R.id.bt_time);
        CalendarView calendarView = holderView.findViewById(R.id.calendar);
        calendarView.setStartEndDate("2010.7", "2049.12")
                .setInitDate("2018.04")
                .setSingleDate(getPresenter().stringDay2Point(data))
                .init();
        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean dateBean) {
                String tow ,three;
                if(dateBean.getSolar()[1]<10){
                    tow = "0"+dateBean.getSolar()[1];
                }else {
                     tow =""+ dateBean.getSolar()[1];
                }
                if(dateBean.getSolar()[2]<10){
                     three = "0"+dateBean.getSolar()[2];
                }else {
                     three = ""+dateBean.getSolar()[2];
                }
                checkTime = "" + dateBean.getSolar()[0] + "-" + tow + "-" + three;
                time.setText(checkTime);
            }
        });
        dialog.show();
    }


    @Override
    protected HistoryActivityPersent createPresenter() {
        return new HistoryActivityPersent();
    }

    @Override
    protected HistoryActivityPersent.Ui getUi() {
        return this;
    }

    private PieData getPieData(int count, Exercise date) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        xValues.add("步行"+date.getStep()+"步消耗"+df.format(date.getStep()* Constant.STEP_KLL)+"千卡");
        xValues.add("仰卧起坐"+date.getSetUp()+"个消耗"+df.format(date.getSetUp()* Constant.SET_UP)+"千卡");
        xValues.add("俯卧撑"+date.getPushUp()+"步消耗"+df.format(date.getPushUp()* Constant.SET_UP)+"千卡");
        //总卡路里
        double totalKLL=date.getStep()* Constant.STEP_KLL +date.getSetUp()* Constant.SET_UP +date.getPushUp()* Constant.SET_UP;

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        //饼图数据
        for(int i =0;i<3;i++){
            float quarterly;
            if(i==0){
                quarterly = (float) (date.getStep()* Constant.STEP_KLL /totalKLL);
            }else if(i==1){quarterly = (float) (date.getSetUp()* Constant.SET_UP /totalKLL);
            }else {
                quarterly = (float) (date.getPushUp()* Constant.SET_UP /totalKLL);
            }

            yValues.add(new Entry(quarterly*100, i));
        }


        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues,data/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        for(int i =0;i<count;i++){
            if(i==0){
                colors.add(Color.BLUE);
            }else if(i==1){
                colors.add(Color.DKGRAY);
            }else {
                colors.add(Color.GREEN);
            }
        }

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }



    @Override
    public void showDay(Exercise date) {
            double totalKLL=date.getStep()* Constant.STEP_KLL +date.getSetUp()* Constant.SET_UP +date.getPushUp()* Constant.SET_UP;

            PieData pieData =getPieData(3,date);
            pieChart.setHoleColorTransparent(true);

            pieChart.setHoleRadius(60f);  //半径
            pieChart.setTransparentCircleRadius(64f); // 半透明圈
            //pieChart.setHoleRadius(0)  //实心圆

            pieChart.setDescription("本日运动量");
            pieChart.setDescriptionTextSize(28);
            // mChart.setDrawYValues(true);
            pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

            pieChart.setDrawHoleEnabled(true);

            pieChart.setRotationAngle(90); // 初始旋转角度

            // draws the corresponding description value into the slice
            // mChart.setDrawXValues(true);

            // enable rotation of the chart by touch
            pieChart.setRotationEnabled(true); // 可以手动旋转

            // display percentage values
            pieChart.setUsePercentValues(true);  //显示成百分比
            // mChart.setUnit(" €");
            // mChart.setDrawUnitsInChart(true);

            // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
            // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

            pieChart.setCenterText("当日运动"+df.format(totalKLL)+"千卡");  //饼状图中间的文字

            //设置数据
            pieChart.setData(pieData);




            // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

            Legend mLegend = pieChart.getLegend();  //设置比例图
            mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
            mLegend.setXEntrySpace(7f);
            mLegend.setYEntrySpace(5f);


            pieChart.animateXY(1000, 1000);  //设置动画
            // mChart.spin(2000, 0, 360);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_history;
    }
}

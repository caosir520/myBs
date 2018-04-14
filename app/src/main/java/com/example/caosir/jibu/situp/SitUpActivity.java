package com.example.caosir.jibu.situp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.caosir.jibu.MainActivity;
import com.example.caosir.jibu.MyApplication;
import com.example.caosir.jibu.R;
import com.example.caosir.jibu.callback.StepCallBack;
import com.example.caosir.jibu.config.Constant;
import com.example.caosir.jibu.config.SettingUtils;
import com.example.caosir.jibu.pojo.Exercise;
import com.example.caosir.jibu.utils.DbUtils;
import com.wise.common.baserx.RxJavaDemoUtil;
import com.wise.common.commonutils.LogUtil;
import com.wise.common.commonutils.TimeUtil;

import java.util.List;

import rx.Subscriber;

/**
 * 创建人: caosir
 * 创建时间：2018/4/8
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class SitUpActivity extends Activity implements StepCallBack {

    private String CURRENTDATE= "";

    Dialog dialog;
    private int setUpNum;
    TextView tv_Num;
    TextView tv_Time;
    ImageView imageView;
    private int time;
    @Override
    public void Step(int p1) {
        setUpNum=p1;
        tv_Num.setText(""+p1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        tv_Num= (TextView)findViewById(R.id.tv_num);
        tv_Time=(TextView) findViewById(R.id.tv_time);
        imageView=(ImageView)findViewById(R.id.image);
        CURRENTDATE = TimeUtil.getOldDate(0);
        showTimeDialog();
        Glide.with(this).load(R.drawable.set_up).asGif().into(imageView);

        ProximitySensor ser = new ProximitySensor(this, this);
        ser.getStep();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        TextView num = (TextView)view.findViewById(R.id.tv_total_num);
        num.setText("你总共做了"+tv_Num.getText()+"个仰卧起坐");
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView2);
        Glide.with(this).load(R.drawable.sleep).asGif().into(imageView);
        dialog = new Dialog(this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        dialog.show();
    }

    private void showTimeDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_time_layout, null);
        final EditText et_time = (EditText) view.findViewById(R.id.et_time);
        Button bt_ok = (Button)view.findViewById(R.id.bt_ok);

        final Dialog dialog1 = new Dialog(this);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        dialog1.setContentView(view);
        dialog1.show();
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.d("数字"+et_time.getEditableText().toString());
                time=Integer.parseInt(et_time.getEditableText().toString());
                RxJavaDemoUtil.CountDowm(new Subscriber<Long>() {

                    public void onCompleted() {
                    }

                    public void onError(Throwable e) {
                    }

                    public void onNext(Long aLong) {
                        if((time - aLong.longValue()) < 0x3) {
                            tv_Time.setTextColor(0xffff0000);
                        }
                        tv_Time.setText((time - aLong.longValue()) + "s");
                        if(aLong==time-1){
                            save();
                            showDialog();
                        }
                    }
                }, time);
                dialog1.cancel();
            }
        });
    }

    private void save() {

        List<Exercise> list = DbUtils.getQueryByWhere(Exercise.class, "date", new String[] {CURRENTDATE});
        LogUtil.d(""+list.size());
        if((list.size() == 0) || (list.isEmpty())) {
            Exercise data = new Exercise();
            data.setDate(CURRENTDATE);
            data.setSetUp(setUpNum);
            data.setUserNamer(SettingUtils.getSharedPreferences(MyApplication.getContext(), Constant.USER_NAME,""));
            DbUtils.insert(data);
        } else if(list.size() == 0x1) {
            Exercise data = (Exercise)list.get(0x0);
            data.setSetUp((data.getSetUp() + setUpNum));
            DbUtils.update(data);
        }
        RxJavaDemoUtil.CountDowm(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Long aLong) {
                if(aLong==2){
                    dialog.cancel();;
                    Intent intent = new Intent(SitUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 0x3);
    }

}

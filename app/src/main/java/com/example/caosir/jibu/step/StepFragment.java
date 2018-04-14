package com.example.caosir.jibu.step;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.caosir.jibu.MainActivity;
import com.example.caosir.jibu.R;
import com.example.caosir.jibu.base.BaseCoreFragment;
import com.example.caosir.jibu.base.BaseCoreMVPFragment;
import com.example.caosir.jibu.base.BasePresenter;
import com.example.caosir.jibu.base.BaseUI;
import com.example.caosir.jibu.base.StepMode;
import com.example.caosir.jibu.callback.StepCallBack;
import com.example.caosir.jibu.config.Constant;
import com.example.caosir.jibu.service.StepInPedometer;
import com.example.caosir.jibu.view.CircleProssView;
import com.example.caosir.jibu.view.SeverDateView;
import com.wise.common.baserx.RxJavaDemoUtil;
import com.wise.common.commonutils.LogUtil;
import com.wise.common.commonutils.RxJavaUtil;
import com.wise.common.commonutils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.internal.framed.FrameReader;
import rx.Observable;
import rx.Subscriber;

/**
 * 创建人: caosir
 * 创建时间：2018/3/8
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class StepFragment extends BaseCoreMVPFragment<StepPresent,StepPresent.StepUI> implements StepPresent.StepUI , Handler.Callback {
    View view;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.step_fragment,container,false);
        return view ;
    }
    CircleProssView circleProssView;
    SeverDateView severDateView;

    @Override
    protected StepPresent createPresenter() {
        return new StepPresent();
    }

    @Override
    protected StepPresent.StepUI getUi() {
        return this;
    }

    TextView tv_last;
    TextView tv_most;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        severDateView =(SeverDateView)view .findViewById(R.id.dateview);
        circleProssView =(CircleProssView)view.findViewById(R.id.progress);
        tv_last = (TextView)view.findViewById(R.id.tv_last);
        tv_last.setText("asdasd");
        tv_most = (TextView)view.findViewById(R.id.tv_most);
        getPresenter().get7Day();
        getPresenter().getMostLast();
        getPresenter().getStep();

//        StepMode stepMode = new StepInPedometer(getActivity(),this);
//        stepMode.getStep();
    }






    @Override
    public void show7Day(final List date) {
        severDateView.setDate(date);
        final List list = date;
        severDateView.setClick(new SeverDateView.Click() {
            @Override
            public void onClick(int i) {
                ToastUtil.showBottomtoast(getActivity(),""+list.get(i)+"步");
            }
        });
    }

    @Override
    public void showToday(int date) {
        circleProssView.setNow(date);
    }

    @Override
    public void showLast(int date) {
        tv_last.setText(""+date);
    }

    @Override
    public void showMost(int date) {
        tv_most.setText(""+date);
    }




    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                // 更新界面上的步数
                LogUtil.d(""+msg.getData().getInt("step"));
                break;
            case Constant.REQUEST_SERVER:
                break;
        }
        return false;
    }
}

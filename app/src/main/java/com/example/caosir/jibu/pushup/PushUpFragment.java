package com.example.caosir.jibu.pushup;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caosir.jibu.R;
import com.example.caosir.jibu.base.BaseCoreMVPFragment;
import com.example.caosir.jibu.situp.SitUpActivity;
import com.example.caosir.jibu.view.CircleProssView;
import com.example.caosir.jibu.view.SeverDateView;
import com.wise.common.commonutils.ToastUtil;

import java.util.List;

/**
 * 创建人: caosir
 * 创建时间：2018/4/8
 * 修改人：
 * 修改时间：
 * 类说明： 仰臥起坐
 */

public class PushUpFragment extends BaseCoreMVPFragment<PushUpPresent,PushUpPresent.UI> implements PushUpPresent.UI {
    @Override
    protected PushUpPresent createPresenter() {
        return new PushUpPresent();
    }

    @Override
    protected PushUpPresent.UI getUi() {
        return this;
    }

    View view;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.step_fragment,container,false);
        return view ;
    }
    CircleProssView circleProssView;
    SeverDateView severDateView;


    TextView tv_last;
    TextView tv_most;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        severDateView =(SeverDateView)view .findViewById(R.id.dateview);
        circleProssView =(CircleProssView)view.findViewById(R.id.progress);
        tv_last = (TextView)view.findViewById(R.id.tv_last);
        tv_most = (TextView)view.findViewById(R.id.tv_most);
        ImageView imageView =(ImageView)view.findViewById(R.id.imageView);
        imageView.setVisibility(View.GONE);
        TextView tv_start = (TextView)view.findViewById(R.id.tv_start);
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PushUpActivity.class);
                getActivity().startActivity(intent);
            }
        });
        tv_start.setVisibility(View.VISIBLE);
        getPresenter().get7Day();
        getPresenter().getMostLast();
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
                ToastUtil.showBottomtoast(getActivity(),""+list.get(i)+"个");
            }
        });
    }

    @Override
    public void showToday(int date) {
        circleProssView.danwei="个";
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
}

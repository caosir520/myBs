package com.example.caosir.jibu;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.example.caosir.jibu.base.BaseCoreActivity;
import com.example.caosir.jibu.base.BaseFragmentAdapter;
import com.example.caosir.jibu.callback.StepCallBack;
import com.example.caosir.jibu.config.Constant;
import com.example.caosir.jibu.history.HistoryActivity;
import com.example.caosir.jibu.login.LoginActivity;
import com.example.caosir.jibu.pushup.PushUpFragment;
import com.example.caosir.jibu.service.StepService;
import com.example.caosir.jibu.situp.SitUpFragment;
import com.example.caosir.jibu.step.StepFragment;
import com.example.caosir.jibu.view.CircleProssView;
import com.wise.common.commonutils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseCoreActivity implements Handler.Callback ,StepCallBack{

    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.iv_history)
    ImageView iv_history;

    @OnClick(R.id.back)
    public void setBack(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private Messenger messenger;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                //获取服务端的messenger
                messenger = new Messenger(service);
                Message msg = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                msg.replyTo = mGetReplyMessenger;
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private Messenger mGetReplyMessenger = new Messenger(new Handler(this));
    CircleProssView circleProssView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Fragment> fragments= new ArrayList<>();
        fragments.add(new StepFragment());
        fragments.add(new PushUpFragment());
        fragments.add(new SitUpFragment());
        List<String> title = new ArrayList<>();
        title.add("步数");
        title.add("俯卧撑");
        title.add("仰卧起坐");
        BaseFragmentAdapter adapter = new BaseFragmentAdapter(getSupportFragmentManager(),fragments,title);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        startServiceForStrategy();
    }

    @OnClick(R.id.iv_history)
    public void setIv_history(){
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }


    private void startServiceForStrategy() {
        if (!isServiceWork(this, StepService.class.getName())) {
            setupService(true);
        } else {
            setupService(false);
        }
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


    /**
     * 启动service
     *
     * @param flag true-bind和start两种方式一起执行 false-只执行bind方式
     */
    private void setupService(boolean flag) {
        Intent intent = new Intent(this, StepService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        if (flag) {
            startService(intent);
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case Constant.MSG_FROM_SERVER:
                // 更新界面上的步数
                LogUtil.d("jjjj"+msg.getData().getInt("step") );
                break;
            case Constant.REQUEST_SERVER:
                try {
                    Message msg1 = Message.obtain(null, Constant.MSG_FROM_CLIENT);
                    msg1.replyTo = mGetReplyMessenger;
                    messenger.send(msg1);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                break;
        }
        return false;
    }

    @Override
    public void Step(int p1) {
        circleProssView.setNow(p1+10);
    }
}

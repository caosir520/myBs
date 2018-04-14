package com.example.caosir.jibu.frist;


import android.content.Intent;
import android.os.Bundle;

import com.example.caosir.jibu.MainActivity;
import com.example.caosir.jibu.R;
import com.example.caosir.jibu.base.BaseCoreMVPActivity;
import com.example.caosir.jibu.login.LoginActivity;

public class FristActivity extends BaseCoreMVPActivity<FristActivityPersent, FristActivityPersent.Ui> implements FristActivityPersent.Ui {

    @Override
    public int getLayoutId() {
        return R.layout.activity_frist;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().getLogin(this);
    }

    @Override
    protected FristActivityPersent createPresenter() {
        return new FristActivityPersent();
    }

    @Override
    protected FristActivityPersent.Ui getUi() {
        return this;
    }

    @Override
    public void startLogin() {
        Intent intent = new Intent(FristActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void startMain() {
        Intent intent = new Intent(FristActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

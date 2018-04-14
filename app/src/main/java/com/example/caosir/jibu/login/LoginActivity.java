package com.example.caosir.jibu.login;


import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.example.caosir.jibu.MainActivity;
import com.example.caosir.jibu.R;
import com.example.caosir.jibu.base.BaseCoreMVPActivity;
import com.example.caosir.jibu.config.Constant;
import com.example.caosir.jibu.config.SettingUtils;
import com.wise.common.commonutils.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseCoreMVPActivity<LoginActivityPersent, LoginActivityPersent.Ui> implements LoginActivityPersent.Ui {


    @BindView(R.id.et_login_name)
    EditText et_name;
    @BindView(R.id.et_login_password)
    EditText et_password;
    @BindView(R.id.bt_login)
    Button bt_login;
    @BindView(R.id.bt_check_inpatientward)
    Button bt_zhuc;



    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @OnClick(R.id.bt_login)
    public void setBt_login(){
        getPresenter().login(et_name.getEditableText().toString(),et_password.getEditableText().toString());
    }

    @OnClick(R.id.bt_check_inpatientward)
    public void setBt_zhuc(){
        getPresenter().zhuc(et_name.getEditableText().toString(),et_password.getEditableText().toString());
    }



    @Override
    protected LoginActivityPersent createPresenter() {
        return new LoginActivityPersent();
    }

    @Override
    protected LoginActivityPersent.Ui getUi() {
        return this;
    }

    @Override
    public void loginError(String error) {
        ToastUtil.showBottomtoast(this,error);


    }

    @Override
    public void loginSuccess() {
        ToastUtil.showBottomtoast(this,"登陆成功");
        SettingUtils.setEditor(this, Constant.IS_LOGIN,true);
        SettingUtils.setEditor(this, Constant.USER_NAME,et_name.getEditableText().toString());
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void zhucError(String error) {
        ToastUtil.showBottomtoast(this,error);
    }

    @Override
    public void zhucSuccess() {
        ToastUtil.showBottomtoast(this,"注册成功");
    }
}

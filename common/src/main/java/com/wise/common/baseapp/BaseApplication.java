package com.wise.common.baseapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.wise.common.commonutils.*;

/**
 * Created by sunpeng on 17/10/13.
 */

public class BaseApplication extends Application{
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        com.wise.common.commonutils.LogUtil.d("Application==Context？"+(this==this.getApplicationContext()));
    }

    public static Context getContext() {

        return instance.getApplicationContext();
    }

    public static Resources getAppResources() {
        return instance.getResources();
    }
}

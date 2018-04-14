package com.example.caosir.jibu.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 创建人: caosir
 * 创建时间：2018/3/9
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, StepService.class);
        context.startService(i);
    }
}

package com.example.caosir.jibu.situp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.caosir.jibu.base.StepMode;
import com.example.caosir.jibu.callback.StepCallBack;
import com.wise.common.commonutils.LogUtil;

/**
 * 创建人: caosir
 * 创建时间：2018/4/9
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class ProximitySensor extends StepMode {
    public ProximitySensor(Context context, StepCallBack stepCallBack) {
        super(context, stepCallBack);
    }


    private int num =0;
    @Override
    protected void registerSensor() {
        Sensor proximtySensor = sensorManager.getDefaultSensor(0x8);
        if(proximtySensor != null) {
            sensorManager.registerListener(this, proximtySensor, 0x2);
            isAvailable = true;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        LogUtil.d("距离传感器"+event.values[0]+""+num);
        if(event.values[0] == 0) {
            num++;
            stepCallBack.Step(num);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensorManager.registerListener(this,detectorSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }
}

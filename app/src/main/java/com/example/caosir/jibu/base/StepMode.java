package com.example.caosir.jibu.base;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.caosir.jibu.callback.StepCallBack;

/**
 * 创建人: caosir
 * 创建时间：2018/3/9
 * 修改人：
 * 修改时间：
 * 类说明：
 */


public abstract class StepMode implements SensorEventListener {
    private Context context;
    public SensorManager sensorManager;
    public StepCallBack stepCallBack;

    protected abstract void registerSensor();

    public static int CURRENT_SETP = 0;
    public boolean isAvailable = false;

    public StepMode(Context context, StepCallBack stepCallBack) {
        this.context = context;
        this.stepCallBack = stepCallBack;
    }

    public boolean getStep() {
        prepareSensorManager();
        registerSensor();
        return isAvailable;
    }

    private void prepareSensorManager() {
        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
        }
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }
}

package com.wise.common.baserx;

import com.wise.common.commonutils.LogUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 创建人: caosir
 * 创建时间：2017/10/18
 * 修改人：
 * 修改时间：
 * 类说明：Rxjava 常用例子工具类
 */

public class RxJavaDemoUtil {
    /**
     * 说明:倒计时的方法
    *@author: caosir
    *@time：2017/10/18
    *@param：订阅者，倒计时的时间
    *@return
    */
    public static void CountDowm(Subscriber<Long> subscriber , int time){
        Observable.interval(0,1, TimeUnit.SECONDS).take(time)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 说明:每次x ms 执行动作
     *@author: caosir
     *@time：2017/10/18
     *@param：订阅者，倒计时的时间
     *@return
     */
    public static void whlieTime(Subscriber<Long> subscriber , int time){
        Observable.interval(0,time, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}

package com.example.caosir.jibu;

import com.example.caosir.jibu.config.Constant;
import com.example.caosir.jibu.utils.DbUtils;
import com.wise.common.baseapp.BaseApplication;

/**
 * 创建人: caosir
 * 创建时间：2018/3/8
 * 修改人：
 * 修改时间：
 * 类说明：
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化数据库
        DbUtils.createDb(this, Constant.DbName);
    }
}

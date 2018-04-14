package com.example.caosir.jibu.frist


import android.content.Context

import com.example.caosir.jibu.base.BasePresenter
import com.example.caosir.jibu.base.BaseUI
import com.example.caosir.jibu.config.Constant
import com.example.caosir.jibu.config.SettingUtils

/**
 * Created by Administrator on 2017/11/27/027.
 */

class FristActivityPersent : BasePresenter<FristActivityPersent.Ui>() {


    fun getLogin(context: Context) {
        if (SettingUtils.getSharedPreferences(context, Constant.IS_LOGIN, false)) {
            ui.startMain()
        } else {
            ui.startLogin()
        }
    }

    interface Ui : BaseUI {
        fun startLogin()
        fun startMain()

    }

}
